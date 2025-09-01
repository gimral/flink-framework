package com.gimral.streaming.core.aop;

import com.gimral.streaming.connector.kafka.LeapKafkaSource;
import com.gimral.streaming.core.configuration.KafkaOptions;
import com.gimral.streaming.core.model.LeapRecord;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

// Class is not used it is included to help with working out the aspect
public class LeapSEE extends StreamExecutionEnvironment {
    public <OUT> DataStreamSource<LeapRecord<OUT>> fromKafkaSource(
            String topic, KafkaOptions kafkaOptions, Class<OUT> valueClass) {
        return fromKafkaSource(topic, kafkaOptions, valueClass, topic + " Kafka Source");
    }

    public <OUT> DataStreamSource<LeapRecord<OUT>> fromKafkaSource(
            String topic, KafkaOptions kafkaOptions, Class<OUT> valueClass, String sourceName) {
        System.out.println("From Kafka");
        KafkaSource<LeapRecord<OUT>> kafkaSource =
                LeapKafkaSource.from(topic, kafkaOptions, valueClass);
        WatermarkStrategy<LeapRecord<OUT>> watermarkStrategy = WatermarkStrategy.noWatermarks();
        return fromSource(kafkaSource, watermarkStrategy, sourceName);
    }

    //    public <OUT> DataStreamSource<LeapRecord<OUT>>
    // StreamExecutionEnvironment.fromKafkaSource(String topic,
    //
    // KafkaOptions kafkaOptions,
    //
    // Class<OUT> valueClass) {
    //        return fromKafkaSource(topic, kafkaOptions, valueClass, topic + " Kafka Source");
    //    }
    //
    //    public <OUT> DataStreamSource<LeapRecord<OUT>>
    // StreamExecutionEnvironment.fromKafkaSource(String topic,
    //
    // KafkaOptions kafkaOptions,
    //
    // Class<OUT> valueClass,
    //
    // String sourceName) {
    //        System.out.println("From Kafka");
    //        KafkaSource<LeapRecord<OUT>> kafkaSource = LeapKafkaSource.from(topic,
    // kafkaOptions,valueClass );
    //        WatermarkStrategy<LeapRecord<OUT>> watermarkStrategy =
    // WatermarkStrategy.noWatermarks();
    //        return fromSource(kafkaSource, watermarkStrategy, sourceName);
    //    }
}
