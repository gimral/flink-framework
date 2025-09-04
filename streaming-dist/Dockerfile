# # Multi-stage Dockerfile
# # Stage 1: build the Java application using Maven
# FROM maven:3.9.4-eclipse-temurin-17 AS builder

# WORKDIR /workspace

# # Copy the whole repository into the build context
# COPY . /workspace

# # Skip tests to keep the image build fast; remove -DskipTests if you want tests.
# RUN --mount=type=cache,target=~/.m2 mvn -DskipTests package


# Stage 2: runtime based on Flink 2.0
FROM flink:2.0.0

LABEL org.opencontainers.image.source="https://example.com/your-repo"
LABEL maintainer="gimral"

# Create userlib directory if not present and copy the built artifact(s).
# We copy any JAR produced by the streaming-core module into the Flink user lib.
# COPY --from=builder /workspace/streaming-dist/target/flink-dist-*.jar /opt/flink/lib/
COPY target/flink-dist-*.jar /opt/flink/lib/

# Ensure files are readable by the default Flink user (UID 1000) if needed.
USER root
RUN chmod -R a+r /opt/flink/lib || true && chown -R flink:flink /opt/flink/lib || true
USER flink

# Do not override the Flink image entrypoint. The application jar(s) will be
# available in /opt/flink/usrlib for the user to submit via the Flink CLI or
# automatically picked up depending on your Flink image configuration.

# Optional: expose ports if your job needs them (commented out by default)
# EXPOSE 8081
