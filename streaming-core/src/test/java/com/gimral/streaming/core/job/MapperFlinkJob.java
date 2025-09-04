//package com.gimral.streaming.core.job;
//
//import com.gimral.streaming.core.LeapFlinkJob;
//import com.gimral.streaming.core.helpers.function.LeapRecordMapFunction;
//import com.gimral.streaming.core.model.LeapEventIntRecord;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.List;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//
//public class MapperFlinkJob extends LeapFlinkJob {
//    private List<LeapEventIntRecord> result;
//
//    @Override
//    public void build(StreamExecutionEnvironment env) {
//        @SuppressWarnings("deprecation")
//        DataStream<LeapEventIntRecord> ds =
//                env.fromData(
//                                Arrays.asList(
//                                        LeapEventIntRecord.getTestRecord(1, 1, "1", "map"),
//                                        LeapEventIntRecord.getTestRecord(2, 1, "2", "map"),
//                                        LeapEventIntRecord.getTestRecord(3, 1, "3", "map"),
//                                        LeapEventIntRecord.getTestRecord(4, 1, "4", "map")))
//                        .setParallelism(1);
//
//        ds = ds.map(new LeapRecordMapFunction());
//        try {
//            Iterator<LeapEventIntRecord> it = ds.executeAndCollect();
//            result = new java.util.ArrayList<>();
//            while (it.hasNext()) {
//                result.add(it.next());
//            }
//            System.out.println(result);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public List<LeapEventIntRecord> getResult() {
//        return result;
//    }
//}
