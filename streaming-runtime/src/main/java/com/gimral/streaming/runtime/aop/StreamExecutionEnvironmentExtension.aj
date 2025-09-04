package com.gimral.streaming.runtime.aop;

import com.gimral.streaming.connector.kafka.LeapKafkaSource;
import com.gimral.streaming.core.configuration.KafkaOptions;
import com.gimral.streaming.core.model.LeapRecord;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public aspect StreamExecutionEnvironmentExtension {
    // Add method directly to DataStream using inter-type declaration
    public <OUT> DataStreamSource<OUT> StreamExecutionEnvironment.fromKafkaSource(OUT... data) {
        System.out.println("From Kafka");
        return fromData(data);
    }

    public <OUT> DataStreamSource<LeapRecord<OUT>> StreamExecutionEnvironment.fromKafkaSource(String topic,
                                                                                              KafkaOptions kafkaOptions,
                                                                                              Class<OUT> valueClass) {
        return fromKafkaSource(topic, kafkaOptions, valueClass, topic + " Kafka Source");
    }

    public <OUT> DataStreamSource<LeapRecord<OUT>> StreamExecutionEnvironment.fromKafkaSource(String topic,
                                                                                              KafkaOptions kafkaOptions,
                                                                                              Class<OUT> valueClass,
                                                                                              String sourceName) {
        System.out.println("From Kafka");
        KafkaSource<LeapRecord<OUT>> kafkaSource = LeapKafkaSource.from(topic, kafkaOptions,valueClass );
        WatermarkStrategy<LeapRecord<OUT>> watermarkStrategy = WatermarkStrategy.noWatermarks();
        return fromSource(kafkaSource, watermarkStrategy, sourceName);
    }
}
