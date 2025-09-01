package com.gimral.streaming.httpclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gimral.streaming.core.model.LeapEvent;
import com.gimral.streaming.core.model.LeapRecord;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Headers;
import okhttp3.Request;

public class DefaultHeaderAssigner<I> implements Serializable {
  private static final ObjectMapper mapper = new ObjectMapper();

  public void applyDefaultHeaders(Request.Builder requestBuilder, I input) {
    Map<String, String> eventHeaders = getEventHeaders(input);
    Headers existingHeaders = requestBuilder.build().headers();
    for (Map.Entry<String, String> header : eventHeaders.entrySet()) {
      if (existingHeaders.get(header.getKey()) == null && header.getValue() != null)
        requestBuilder.header(header.getKey(), header.getValue());
    }
  }

  private Map<String, String> getEventHeaders(I e) {
    Map<String, String> headers = new HashMap<>();
    if (e instanceof LeapRecord<?> leapRecord) {
      if (leapRecord.getValue() instanceof LeapEvent<?> leapEvent) {
        headers.put("URC", leapEvent.getUrc());
        // headers.put("Financial-Id",leapEvent.getFinancialId());
        return headers;
      }
      JsonNode jsonNode = parseLeapReord(leapRecord);
      if (jsonNode == null) return headers;
      headers.put("URC", jsonNode.get("urc").asText());
      // headers.put("Financial-Id",jsonNode.get("financialId").asText());
      return headers;
    }
    return headers;
  }

  private JsonNode parseLeapReord(LeapRecord<?> leapRecord) {
    if (leapRecord.getValue() instanceof JsonNode jsonNode) return jsonNode;
    try {
      return mapper.valueToTree(leapRecord);
    } catch (Exception ex) {
      return null;
    }
  }
}
