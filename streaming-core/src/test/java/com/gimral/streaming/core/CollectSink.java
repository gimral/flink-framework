/* (C) 2025 */
package com.gimral.streaming.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.flink.api.connector.sink2.Sink;
import org.apache.flink.api.connector.sink2.SinkWriter;
import org.apache.flink.api.connector.sink2.WriterInitContext;

public class CollectSink<T> implements Sink<T> {
    public static final List<Object> values = Collections.synchronizedList(new ArrayList<>());

    @Override
    public SinkWriter<T> createWriter(WriterInitContext context) {
        return new CollectSinkWriter<>();
    }

    private static class CollectSinkWriter<T> implements SinkWriter<T> {
        @Override
        public void write(T element, Context context) {
            values.add(element);
        }

        @Override
        public void flush(boolean endOfInput) {
            // No-op: No flushing needed for testing
        }

        @Override
        public void close() {
            // No-op: No resources to close
        }
    }
}
