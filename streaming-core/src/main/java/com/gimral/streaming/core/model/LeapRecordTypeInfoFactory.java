package com.gimral.streaming.core.model;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInfoFactory;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.typeutils.PojoField;
import org.apache.flink.api.java.typeutils.PojoTypeInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

public class LeapRecordTypeInfoFactory extends TypeInfoFactory<LeapRecord> {

    @Override
    public TypeInformation<LeapRecord> createTypeInfo(Type t, Map<String, TypeInformation<?>> genericParameters) {
        try {
            // Get TypeInformation for the generic parameter V
            TypeInformation<?> valueType = genericParameters.getOrDefault("V", Types.BYTE);

            // Define fields for PojoTypeInfo
            Field metadataField = LeapRecord.class.getDeclaredField("metadata");
            Field keyField = LeapRecord.class.getDeclaredField("key");
            Field valueField = LeapRecord.class.getDeclaredField("value");

            PojoField[] fields = new PojoField[] {
                    new PojoField(metadataField, Types.OBJECT_ARRAY(TypeInformation.of(LeapMetaData.class))),
                    new PojoField(keyField, Types.STRING),
//                    new PojoField(valueField, Types.)
            };

            // Create PojoTypeInfo for LeapInternalRecord
            return new PojoTypeInfo<>(LeapRecord.class, Arrays.asList(fields));

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
