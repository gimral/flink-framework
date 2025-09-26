# Multi-stage Dockerfile
# Stage 1: Extract the flink dist library
FROM flink:2.0.0 AS flink-dist

# Stage 2: build the Java application using Maven
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /workspace

# Copy the whole repository into the build context
COPY . /workspace

COPY --from=flink-dist /opt/flink/lib/flink-dist-*.jar /workspace/

# Skip tests to keep the image build fast; remove -DskipTests if you want tests.
RUN --mount=type=cache,target=/root/.m2 mvn -f streaming-logger -DskipTests install
RUN --mount=type=cache,target=/root/.m2 mvn -f streaming-core -DskipTests clean install
RUN --mount=type=cache,target=/root/.m2 mvn -f streaming-connector-kafka -DskipTests clean install
RUN --mount=type=cache,target=/root/.m2 mvn -f streaming-runtime -DskipTests clean install

RUN mvn deploy:deploy-file -DgroupId=org.apache.flink -DartifactId=flink-dist -Dversion=2.0.0 -Durl=file:/workspace/streaming-dist/local-maven-repo/ -DrepositoryId=local-maven-repo -DupdateReleaseInfo=true -Dfile=flink-dist-2.0.0.jar

RUN --mount=type=cache,target=/root/.m2 mvn -f streaming-dist -DskipTests package

RUN mvn dependency:copy -DexcludeTransitive=true -Dartifact=org.apache.logging.log4j:log4j-layout-template-json:2.24.1 -DoutputDirectory=/workspace/libs
RUN mvn dependency:copy -DexcludeTransitive=true -Dartifact=org.apache.flink:flink-connector-kafka:4.0.0-2.0 -DoutputDirectory=/workspace/libs
RUN mvn dependency:copy -DexcludeTransitive=true -Dartifact=org.apache.kafka:kafka-clients:4.0.0 -DoutputDirectory=/workspace/libs


# Stage 3: runtime based on Flink 2.0
FROM flink:2.0.0

# We copy any JAR produced by the streaming-runtime module into the Flink lib.
COPY --from=build /workspace/streaming-dist/target/flink-dist-*.jar /opt/flink/lib/
COPY --from=build /workspace/streaming-dist/target/flink-dist-*.jar /opt/flink/lib/
COPY --from=build /workspace/libs/* /opt/flink/lib/

# Ensure files are readable by the default Flink user (UID 1000) if needed.
USER root
RUN chmod -R a+r /opt/flink/lib || true && chown -R flink:flink /opt/flink/lib || true
USER flink

# Do not override the Flink image entrypoint. The application jar(s) will be
# available in /opt/flink/usrlib for the user to submit via the Flink CLI or
# automatically picked up depending on your Flink image configuration.

# Optional: expose ports if your job needs them (commented out by default)
# EXPOSE 8081
