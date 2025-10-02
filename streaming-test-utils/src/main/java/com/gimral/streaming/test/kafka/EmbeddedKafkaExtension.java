package com.gimral.streaming.test.kafka;

import io.github.embeddedkafka.EmbeddedKafka;
import io.github.embeddedkafka.EmbeddedKafka$;
import io.github.embeddedkafka.EmbeddedKafkaConfigImpl;
import io.github.embeddedkafka.ops.AdminOps;
import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.immutable.HashMap$;

public class EmbeddedKafkaExtension implements
        BeforeAllCallback,
        BeforeEachCallback,
        AfterEachCallback,
        AfterAllCallback,
        ParameterResolver {
    private final static Logger logger = LoggerFactory.getLogger(EmbeddedKafkaExtension.class);

    private final EmbeddedKafkaConfiguration embeddedKafkaConfiguration;
    private EmbeddedKafkaConfigImpl kafkaConfig;
    private EmbeddedKafka$ kafkaCluster;

    public EmbeddedKafkaExtension() {
        this(new EmbeddedKafkaConfiguration.Builder().build());
    }

    public EmbeddedKafkaExtension(EmbeddedKafkaConfiguration embeddedKafkaConfiguration) {
        this.embeddedKafkaConfiguration  = embeddedKafkaConfiguration;
    }

    public EmbeddedKafka$ getKafkaCluster() {
        return kafkaCluster;
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        kafkaConfig = new EmbeddedKafkaConfigImpl(
                embeddedKafkaConfiguration.getKafkaPort(),
                embeddedKafkaConfiguration.getZookeeperPort(),
                HashMap$.MODULE$.empty(),
                HashMap$.MODULE$.empty(),
                HashMap$.MODULE$.empty());

        EmbeddedKafka.start(kafkaConfig);

        kafkaCluster = EmbeddedKafka$.MODULE$;
    }

    @Override
    public void beforeEach(ExtensionContext context) {

    }

    @Override
    public void afterAll(ExtensionContext context) {
        if(kafkaCluster == null)
            return;
        try {
            kafkaCluster.stop();
        }
        catch (Exception ex){
            logger.warn("Could not properly shut down the Kafka Cluster.", ex);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {

    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        Class<? extends java.lang.annotation.Annotation>[] clazzes = new Class[]{InjectKafkaCluster.class,
                InjectAdminOps.class,InjectProducerOps.class, InjectConsumerOps.class};
        for (Class<? extends java.lang.annotation.Annotation> clazz : clazzes) {
            if (parameterContext.isAnnotated((clazz)) && parameterType.isAssignableFrom(parameterType)) {
                return true;
            }
        }
//        if (parameterContext.isAnnotated(InjectKafkaCluster.class)
//                && InjectKafkaCluster.class.isAssignableFrom(parameterType)) {
//            return true;
//        }
        return false;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if(parameterContext.isAnnotated(InjectKafkaCluster.class) ||
                parameterContext.isAnnotated(InjectAdminOps.class) ||
                parameterContext.isAnnotated(InjectProducerOps.class) ||
                parameterContext.isAnnotated(InjectConsumerOps.class)) {
            return kafkaCluster;
        }
        throw new ParameterResolutionException("Unsupported parameter");
    }
}
