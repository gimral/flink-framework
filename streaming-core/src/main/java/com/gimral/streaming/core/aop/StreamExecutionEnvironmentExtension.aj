package com.gimral.streaming.core.aop;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Collection;

public aspect StreamExecutionEnvironmentExtension {
    // Add method directly to DataStream using inter-type declaration
    public <OUT> DataStreamSource<OUT> StreamExecutionEnvironment.fromKafkaSource(OUT... data) {
        System.out.println("From Kafka");
        return fromData(data);
    }
}
