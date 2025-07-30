package com.gimral.streaming.connector.kafka;

import com.gimral.streaming.core.model.LeapEvent;
import com.gimral.streaming.core.model.LeapRecord;
import io.github.embeddedkafka.EmbeddedKafka$;
import io.github.embeddedkafka.EmbeddedKafkaConfigImpl;
import io.github.embeddedkafka.EmbeddedKafka;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.runtime.testutils.MiniClusterResourceConfiguration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.test.junit5.MiniClusterExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import scala.collection.immutable.HashMap;
import scala.collection.immutable.HashMap$;

import java.util.Arrays;

public class LeapKafkaSourceTest {
    @RegisterExtension
    public static final MiniClusterExtension MINI_CLUSTER = new MiniClusterExtension(
            new MiniClusterResourceConfiguration.Builder()
                    .setNumberTaskManagers(1)
                    .setNumberSlotsPerTaskManager(2)
                    .build());

    private final static int KAFKA_PORT = 6001;
    private final static int ZOOKEEPER_PORT = 6002;
    private final static String TEST_TOPIC = "test-topic";
    private static EmbeddedKafkaConfigImpl kafkaConfig;
    private static EmbeddedKafka$ kafkaOps;

    @BeforeAll
    public static void setup() {
        kafkaConfig = new EmbeddedKafkaConfigImpl(
                KAFKA_PORT,
                ZOOKEEPER_PORT,
                HashMap$.MODULE$.empty(),
                HashMap$.MODULE$.empty(),
                HashMap$.MODULE$.empty());
        EmbeddedKafka.start(kafkaConfig);

        kafkaOps = EmbeddedKafka$.MODULE$;

        kafkaOps.createCustomTopic(TEST_TOPIC,new HashMap<>(),2,1, kafkaConfig);
    }

    @Test
    public void testDeserialization() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();



//        KafkaSource<LeapRecord<String>> source = LeapKafkaSource.from(TEST_TOPIC, "localhost:" + KAFKA_PORT, String.class);
//
//        DataStream<LeapRecord<String>> ds =  env.
//                fromSource(source, WatermarkStrategy.noWatermarks(), "kafkaSource");

        DataStream<LeapRecord<Integer>> ds = env.fromData(Arrays.asList(
                getInternalTestRecord(1,1,"1","map"),
                getInternalTestRecord(2,1,"2","map"),
                getInternalTestRecord(3,1,"3","map"),
                getInternalTestRecord(4,1,"4","map")
        )).setParallelism(1);

        ds = ds.map(new MapFunction<LeapRecord<Integer>, LeapRecord<Integer>>() {
            @Override
            public LeapRecord<Integer> map(LeapRecord<Integer> r) throws Exception {
                System.out.println("Received record: " + r);
                return r;
            }
        });

//        ds = ds.map(r -> {
//            System.out.println("Received record: " + r);
//            return r;
//        })
////                .returns(TypeInformation.of(new TypeHint<LeapRecord<Integer>>() {
////        }))
//        ;

        env.execute();

    }

    private LeapRecord<Integer> getInternalTestRecord(Integer data, long timestamp, String urc, String type){
        LeapRecord<Integer> record = new LeapRecord<Integer>();
        record.setValue(data);
        return record;
    }

    private LeapRecord<LeapEvent<Integer>> getTestRecord(Integer data, long timestamp, String urc, String type){
        LeapRecord<LeapEvent<Integer>> record = new LeapRecord<LeapEvent<Integer>>();
        LeapEvent<Integer> value = new LeapEvent<>();
        value.setData(data);
        value.setUrc(urc);
        value.setType(type);
        value.setTimestamp(timestamp);
        record.setValue(value);
        return record;
    }
}
