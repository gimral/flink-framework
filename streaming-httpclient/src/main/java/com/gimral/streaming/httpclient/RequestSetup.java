package com.gimral.streaming.httpclient;

import java.io.Serializable;
import okhttp3.Request;

@FunctionalInterface
public interface RequestSetup<I> extends Serializable {
  /**
   * This method is called to set up the HTTP request.
   *
   * @param requestBuilder The Request.Builder instance to configure the HTTP request.
   * @throws Exception If an error occurs during setup.
   */
  public void setup(Request.Builder requestBuilder, I input) throws Exception;
}
