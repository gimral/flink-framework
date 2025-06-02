package com.gimral.streaming.core.functions;

import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

import com.gimral.streaming.core.model.ErrorInputOutput;

public class ErrorRouterProcess<IN, OUT> extends ProcessFunction<ErrorInputOutput<IN, OUT>, OUT> {
    @Override
    public void processElement(ErrorInputOutput<IN, OUT> value, Context ctx, Collector<OUT> out) throws Exception {
        if (value.getType() == ErrorInputOutput.ErrorInputOutputType.SUCCESS) {
            // If the input is successful, emit the output
            out.collect(value.getOutput());
        } else {
            // Handle error case if needed
            // For now, we just ignore the error input
        }
    }

}
