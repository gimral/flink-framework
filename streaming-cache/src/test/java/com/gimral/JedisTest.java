package com.gimral;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.json.Path2;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class JedisTest {
    private static final ObjectMapper objectMapper =  new ObjectMapper();
    private static UnifiedJedis jedis;
    private static String key;
    @BeforeAll
    public static void setup() throws JsonProcessingException {
        jedis = new UnifiedJedis("redis://localhost:6379");
        key = "jsonTest:1";
        jedis.jsonSet(key,getTestRecord());
    }

    @AfterAll
    public static void cleanUp(){
        jedis.close();
    }
    @Test
    public void assertNonExistingFieldReturnEmpty(){
        Map<String,String> result = jsonGet(key,"i");
        Assertions.assertEquals(0,result.size());
    }

    @Test
    public void assertSingleFieldReturns(){
        Map<String,String> result = jsonGet(key,"id");
        Assertions.assertEquals(1,result.size());
        Assertions.assertEquals("1",result.get("id"));
    }

    @Test
    public void assertMultiNestedFieldReturns(){
        Map<String,String> result = jsonGet(key,"id", "balance.balance");
        Assertions.assertEquals(2,result.size());
        Assertions.assertEquals("1",result.get("id"));
        Assertions.assertEquals("100",result.get("balance.balance"));
    }

    @Test
    public void assertMultiNestedFieldIncorrectFieldReturns(){
        Map<String,String> result = jsonGet(key,"i", "balance.balance");
        Assertions.assertEquals(2,result.size());
        Assertions.assertEquals("100",result.get("balance.balance"));
    }


    private Map<String,String> jsonGet(String key,String... fieldNames){
        try(UnifiedJedis jedis = new UnifiedJedis("redis://localhost:6379"))
        {
            Path2[] paths = Arrays.stream(fieldNames).map(Path2::of).toArray(Path2[]::new);
            Object val = jedis.jsonGet(key, paths);
            if(val == null)
                return Collections.emptyMap();
            if(val instanceof JSONArray valArray) {
                if (valArray.isEmpty())
                    return Collections.emptyMap();
                return Map.of(fieldNames[0], valArray.optString(0, ""));
            }
            if(val instanceof JSONObject valObject)
                return valObject.keySet().stream().collect(Collectors.toMap(
                    k -> k.substring(2),
                    k -> valObject.optJSONArray(k, new JSONArray()).optString(0,"")
                ));
            return Collections.emptyMap();
        }
    }

    private static String getTestRecord() throws JsonProcessingException {
        return objectMapper.writeValueAsString(
                new AccountExtension(1,"Acc1",new AccountBalance(100)));
    }
}
