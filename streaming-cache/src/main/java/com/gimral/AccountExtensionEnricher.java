package com.gimral;

import org.apache.flink.api.common.functions.MapFunction;

public class AccountExtensionEnricher<I,O> implements MapFunction<I, O> {

    private final CacheMapper<I,O,String> mapper;

    public AccountExtensionEnricher(CacheMapper<I,O,String> mapper) {
        // Constructor logic if needed
        this.mapper = mapper;
    }

    @Override
    public O map(I value) throws Exception {
        //Redis Call
        //Bawaba Call
        //Retries
        String s = "Enriching value: " + value.toString();


        return mapper.map(value, s);
    }
}
