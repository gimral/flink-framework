package com.gimral.streaming.connector.kafka;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.gimral.streaming.core.model.LeapEvent;
import com.gimral.streaming.core.model.LeapRecord;
import com.gimral.streaming.core.model.LeapMetaData;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.connector.kafka.util.JacksonMapperFactory;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;

public class LeapJSONDeserializationSchema<T> implements KafkaRecordDeserializationSchema<LeapRecord<T>> {
    private final Class<T> valueClass;
    private ObjectMapper mapper;

    public LeapJSONDeserializationSchema(Class<T> valueClass) {
        this.valueClass = valueClass;
    }

    @Override
    public void open(DeserializationSchema.InitializationContext context) throws Exception {
        mapper = JacksonMapperFactory.createObjectMapper();
    }

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> consumerRecord, Collector<LeapRecord<T>> collector)
            throws IOException {
        byte[] valueBytes = consumerRecord.value();

        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType recordType = typeFactory.constructParametricType(LeapEvent.class, valueClass);

        // Deserialize value to T
        T value = mapper.readValue(valueBytes, valueClass);

        // Create LeapInternalRecord and set value
        LeapRecord<T> record = new LeapRecord<>();
        record.setValue(value);

        // Set key
        byte[] keyBytes = consumerRecord.key();
        Object key = null;
        if (keyBytes != null) {
            try {
                key = mapper.readValue(keyBytes, Object.class);
            } catch (Exception e) {
                // fallback to raw bytes if not JSON
                key = keyBytes;
            }
        }
        record.setKey(String.valueOf(key));

        // Set metadata
        LeapMetaData meta = new LeapMetaData();
        meta.setSource(consumerRecord.topic());
        meta.setPartition(String.valueOf(consumerRecord.partition()));
        meta.setOffset(String.valueOf(consumerRecord.offset()));
        meta.setTimestamp(String.valueOf(consumerRecord.timestamp()));
        record.setMetadata(new com.gimral.streaming.core.model.LeapMetaData[] { meta });

        collector.collect(record);
    }

    @Override
    public TypeInformation<LeapRecord<T>> getProducedType() {
        return TypeExtractor.getForClass((Class<LeapRecord<T>>) (Class<?>) LeapRecord.class);
    }
}
