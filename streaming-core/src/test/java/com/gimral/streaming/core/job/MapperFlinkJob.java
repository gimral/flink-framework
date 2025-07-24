package com.gimral.streaming.core.job;

import com.gimral.streaming.core.LeapFlinkJob;
import com.gimral.streaming.core.helpers.function.LeapRecordMapFunction;
import com.gimral.streaming.core.model.LeapRecord;
import com.gimral.streaming.core.model.LogLeapEvent;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MapperFlinkJob extends LeapFlinkJob {

    private List<LeapRecord<LogLeapEvent>> result;

    @Override
    public void build(StreamExecutionEnvironment env) {
        @SuppressWarnings("deprecation")
        DataStream<LeapRecord<LogLeapEvent>> ds = env.fromData(Arrays.asList(
                LogLeapEvent.getTestRecord(1,1,"1","map"),
                LogLeapEvent.getTestRecord(2,1,"2","map"),
                LogLeapEvent.getTestRecord(3,1,"3","map"),
                LogLeapEvent.getTestRecord(4,1,"4","map")
        )).setParallelism(1);

        ds = ds.map(new LeapRecordMapFunction());
        try {
            Iterator<LeapRecord<LogLeapEvent>> it = ds.executeAndCollect();
            result = new java.util.ArrayList<>();
            while (it.hasNext()) {
                result.add(it.next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<LeapRecord<LogLeapEvent>> getResult() {
        return result;
    }
}
