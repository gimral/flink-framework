package com.gimral;

import java.io.IOException;
import java.io.Serializable;

@FunctionalInterface
public interface CacheEnricherKeyProvider<I> extends Serializable {
  /**
   * This method is called when the Cache request is successful.
   */
  String getKey(I input);
}
