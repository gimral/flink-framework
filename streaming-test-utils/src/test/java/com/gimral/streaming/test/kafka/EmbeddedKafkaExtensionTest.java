package com.gimral.streaming.test.kafka;

import io.github.embeddedkafka.EmbeddedKafka$;
import io.github.embeddedkafka.ops.AdminOps;
import io.github.embeddedkafka.ops.ConsumerOps;
import io.github.embeddedkafka.ops.ProducerOps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(EmbeddedKafkaExtension.class)
public class EmbeddedKafkaExtensionTest {

    @Test
    public void testClusterInjection(@InjectKafkaCluster EmbeddedKafka$ kafkaCluster) {
        // Just to check if the extension is working fine
        assertNotNull(kafkaCluster);
    }

    @Test
    public void testAdminOpsInjection(@InjectAdminOps AdminOps<?> adminOps) {
        // Just to check if the extension is working fine
        assertNotNull(adminOps);
    }

    @Test
    public void testProducerOpsInjection(@InjectProducerOps ProducerOps<?> producerOps) {
        // Just to check if the extension is working fine
        assertNotNull(producerOps);
    }

    @Test
    public void testConsumerOpsInjection(@InjectConsumerOps ConsumerOps<?> consumerOps) {
        // Just to check if the extension is working fine
        assertNotNull(consumerOps);
    }

    @Test
    public void testMultipleOpsInjection(@InjectAdminOps AdminOps<?> adminOps,
                                         @InjectProducerOps ProducerOps<?> producerOps,
                                         @InjectConsumerOps ConsumerOps<?> consumerOps) {
        // Just to check if the extension is working fine
        assertNotNull(adminOps);
        assertNotNull(producerOps);
        assertNotNull(consumerOps);
    }
}
