package com.gimral.streaming.httpclient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gimral.streaming.core.model.LeapEvent;
import com.gimral.streaming.core.model.LeapRecord;
import okhttp3.Request;
import org.junit.jupiter.api.Test;

public class DefaultHeaderAssignerTest {
  @Test
  public void testApplyDefaultHeaders_withNonLeapInput_noHeadersAdded() {
    DefaultHeaderAssigner<String> assigner = new DefaultHeaderAssigner<>();
    Request.Builder builder = new Request.Builder().url("http://localhost/");

    // Use a plain String (not a LeapRecord) - assigner should not add any headers
    assigner.applyDefaultHeaders(builder, "plain-input");

    Request request = builder.build();
    assertEquals(
        0, request.headers().size(), "No headers should be added for non-LeapRecord input");
  }

  @Test
  public void testApplyDefaultHeaders_existingHeaderNotOverwritten() {
    DefaultHeaderAssigner<String> assigner = new DefaultHeaderAssigner<>();
    Request.Builder builder =
        new Request.Builder().url("http://localhost/").header("URC", "existing-value");

    // Use a plain String (not a LeapRecord). Existing header should remain
    // unchanged.
    assigner.applyDefaultHeaders(builder, "plain-input");

    Request request = builder.build();
    assertEquals(
        "existing-value",
        request.header("URC"),
        "Existing URC header should not be overwritten by the assigner");
  }

  @Test
  public void testApplyDefaultHeaders_withLeapEvent_addsUrcHeader() {
    DefaultHeaderAssigner<LeapRecord<?>> assigner = new DefaultHeaderAssigner<>();
    // create a LeapEvent and wrap it in a LeapRecord
    LeapEvent<String> leapEvent = new LeapEvent<>();
    leapEvent.setUrc("urc-from-event");
    LeapRecord<LeapEvent<String>> record = new LeapRecord<>();
    record.setValue(leapEvent);

    Request.Builder builder = new Request.Builder().url("http://localhost/");

    assigner.applyDefaultHeaders(builder, record);

    Request request = builder.build();
    assertEquals(
        "urc-from-event", request.header("URC"), "URC header should be added from LeapEvent");
  }

  @Test
  public void testApplyDefaultHeaders_withLeapRecordContainingJsonNode_addsUrcHeader() {
    DefaultHeaderAssigner<LeapRecord<?>> assigner = new DefaultHeaderAssigner<>();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.createObjectNode().put("urc", "urc-from-json");
    LeapRecord<JsonNode> record = new LeapRecord<>();
    record.setValue(node);

    Request.Builder builder = new Request.Builder().url("http://localhost/");

    assigner.applyDefaultHeaders(builder, record);

    Request request = builder.build();
    assertEquals(
        "urc-from-json",
        request.header("URC"),
        "URC header should be extracted from JsonNode value");
  }

  @Test
  public void testApplyDefaultHeaders_withLeapRecord_existingHeaderNotOverwritten() {
    DefaultHeaderAssigner<LeapRecord<?>> assigner = new DefaultHeaderAssigner<>();
    // create a LeapEvent with a URC and wrap it in a LeapRecord
    LeapEvent<String> leapEvent = new LeapEvent<>();
    leapEvent.setUrc("urc-from-event");
    LeapRecord<LeapEvent<String>> record = new LeapRecord<>();
    record.setValue(leapEvent);

    // Builder already has a URC header set; assigner should not overwrite it
    Request.Builder builder =
        new Request.Builder().url("http://localhost/").header("URC", "existing-value");

    assigner.applyDefaultHeaders(builder, record);

    Request request = builder.build();
    assertEquals(
        "existing-value",
        request.header("URC"),
        "Existing URC header should not be overwritten when input has URC");
  }
}
