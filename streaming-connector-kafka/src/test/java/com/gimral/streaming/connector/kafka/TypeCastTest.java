package com.gimral.streaming.connector.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gimral.streaming.core.model.LeapRecord;
import com.gimral.streaming.core.model.LeapRecordObjectNode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class TypeCastTest {


    @Test
    public void reflectionTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        long start = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            LeapRecordObjectNode obj = new LeapRecordObjectNode(); // Direct instantiation
        }
        long directTime = System.nanoTime() - start;
        start = System.nanoTime();
        Constructor<LeapRecordObjectNode> constructor = LeapRecordObjectNode.class.getDeclaredConstructor();
        for (int i = 0; i < 1000000; i++) {
            LeapRecordObjectNode obj = constructor.newInstance(); // Reflection
        }
        long reflectionTime = System.nanoTime() - start;
        start = System.nanoTime();
        Supplier<LeapRecordObjectNode> supplier = LeapRecordObjectNode::new;
        for (int i = 0; i < 1000000; i++) {
            LeapRecordObjectNode obj = supplier.get(); // Supplier
        }
        long supplierTime = System.nanoTime() - start;
        System.out.println("Direct: " + directTime / 1_000_000 + " ms");
        System.out.println("Reflection: " + reflectionTime / 1_000_000 + " ms");
        System.out.println("Supplier: " + supplierTime / 1_000_000 + " ms");
    }
    @Test
    public void reflectionTest2() throws Exception {
        long start = System.nanoTime();
        for (int i = 0; i < 1000000; i++) {
            LeapRecord<ObjectNode> obj = new LeapRecord<ObjectNode>(); // Direct instantiation
        }
        long directTime = System.nanoTime() - start;
        start = System.nanoTime();
        Constructor<LeapRecord> constructor = LeapRecord.class.getDeclaredConstructor();
        for (int i = 0; i < 1000000; i++) {
            LeapRecord<?> obj = constructor.newInstance(); // Reflection
        }
        long reflectionTime = System.nanoTime() - start;
        start = System.nanoTime();
        Supplier<LeapRecord<?>> supplier = LeapRecord::new;
        for (int i = 0; i < 1000000; i++) {
            LeapRecord<?> obj = supplier.get(); // Supplier
        }
        long supplierTime = System.nanoTime() - start;
        System.out.println("Direct: " + directTime / 1_000_000 + " ms");
        System.out.println("Reflection: " + reflectionTime / 1_000_000 + " ms");
        System.out.println("Supplier: " + supplierTime / 1_000_000 + " ms");
    }
}
