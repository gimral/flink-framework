package com.gimral;

import java.io.IOException;
import java.io.Serializable;

@FunctionalInterface
public interface CacheEnricherErrorHandler<I, O> extends Serializable {
  /**
   * This method is called when the Cache request is not successful.
   */
  O onError(I input) throws IOException;
}
