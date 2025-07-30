package com.gimral.streaming.httpclient;

import java.io.Serializable;

@FunctionalInterface
public interface ErrorHandler<O> extends Serializable {
  /**
   * This method is called when the HTTP request is successful.
   *
   * @param result The response from the HTTP request.
   */
  O onError();
}
