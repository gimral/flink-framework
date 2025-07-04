package com.gimral.streaming.core.aop;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

//Class is not used it is included to help with working out the aspect
public class LeapSEE extends StreamExecutionEnvironment {
    public <OUT> DataStreamSource<OUT> fromKafkaSource(OUT... data) {
        System.out.println("From Kafka");
        return fromData(data);
    }
}
