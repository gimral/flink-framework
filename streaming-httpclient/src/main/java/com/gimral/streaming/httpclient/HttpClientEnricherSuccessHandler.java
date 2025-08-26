package com.gimral.streaming.httpclient;

import java.io.IOException;
import java.io.Serializable;
import okhttp3.ResponseBody;

@FunctionalInterface
public interface HttpClientEnricherSuccessHandler<I, O> extends Serializable {
  /**
   * This method is called when the HTTP request is successful.
   *
   * @param result The response from the HTTP request.
   */
  O onSuccess(ResponseBody result, I input) throws IOException;
}
