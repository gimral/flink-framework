package com.gimral.streaming.core.job;

import com.gimral.streaming.core.LeapFlinkJob;
import com.gimral.streaming.core.function.FlinkMapFunction;
import com.gimral.streaming.core.model.LeapRecord;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MapperFlinkJob extends LeapFlinkJob {

    private List<LeapRecord<Integer>> result;

    @Override
    public void build(StreamExecutionEnvironment env) {
        @SuppressWarnings("deprecation")
        DataStream<LeapRecord<Integer>> ds = env.fromData(Arrays.asList(
                new LeapRecord<>(1,1,"1","map"),
                new LeapRecord<>(2,1,"2","map"),
                new LeapRecord<>(3,1,"3","map"),
                new LeapRecord<>(4,1,"4","map")
        )).setParallelism(1);

        ds = ds.map(new FlinkMapFunction());
        try {
            Iterator<LeapRecord<Integer>> it = ds.executeAndCollect();
            result = new java.util.ArrayList<>();
            while (it.hasNext()) {
                result.add(it.next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<LeapRecord<Integer>> getResult() {
        return result;
    }
}
