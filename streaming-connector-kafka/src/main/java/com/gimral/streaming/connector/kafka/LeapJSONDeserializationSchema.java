package com.gimral.streaming.connector.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gimral.streaming.core.model.LeapRecord;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.connector.kafka.util.JacksonMapperFactory;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class LeapJSONDeserializationSchema implements KafkaRecordDeserializationSchema<LeapRecord<ObjectNode>> {
    private ObjectMapper mapper;
    @Override
    public void open(DeserializationSchema.InitializationContext context) throws Exception {
        mapper = JacksonMapperFactory.createObjectMapper();
    }

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> consumerRecord, Collector<LeapRecord<ObjectNode>> collector) throws IOException {

    }

    @Override
    public TypeInformation<LeapRecord<ObjectNode>> getProducedType() {
        return TypeExtractor.getForClass(LeapRecord<ObjectNode>.class);
    }
}
