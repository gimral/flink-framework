package com.gimral;

import java.io.IOException;
import java.io.Serializable;

@FunctionalInterface
public interface SuccessHandler<I, O, T> extends Serializable {
  /**
   * This method is called when the Cache request is successful.
   *
   * @param result The response from the cache request.
   */
  O onSuccess(T result, I input) throws IOException;
}
