package com.gimral.streaming.connector.kafka;

import com.gimral.streaming.core.model.LeapEvent;
import com.gimral.streaming.core.model.LeapRecord;
import org.apache.flink.connector.kafka.source.KafkaSource;

public class LeapKafkaSource {
    public static <T> KafkaSource<LeapRecord<T>> from(String topic, String bootstrapServers, Class<T> valueClass) {
        // Implementation for creating a Kafka source
        // This method should return a configured Kafka source for the specified topic and bootstrap servers
        return KafkaSource.<LeapRecord<T>>builder()
                .setTopics(topic)
                .setBootstrapServers(bootstrapServers)
                .setGroupId("leap-kafka-source-group")
                .setDeserializer(new LeapJSONDeserializationSchema<T>(valueClass)) // Adjust the deserializer as needed
                .build();
    }
}
