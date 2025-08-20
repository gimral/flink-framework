package com.gimral.streaming.httpclient;

import java.io.Serializable;

@FunctionalInterface
public interface ErrorHandler<I, O> extends Serializable {
  /**
   * This method is called when the HTTP request is successful.
   */
  O onError(I input);
}
