package com.gimral.streaming.logger;

import java.util.regex.Pattern;

class MaskingPattern {
  private Pattern pattern;
  private String replacement;

  // Default constructor for Jackson
  public MaskingPattern() {}

  // Jackson will use this setter for pattern as String
  public void setPattern(String pattern) {
    validatePattern(pattern);
    this.pattern = Pattern.compile(pattern);
  }

  public void setReplacement(String replacement) {
    this.replacement = replacement;
  }

  public Pattern getPattern() {
    return pattern;
  }

  public String getReplacement() {
    return replacement;
  }

  private void validatePattern(String pattern) {
    if (pattern.trim().isEmpty()) {
      throw new IllegalArgumentException("Pattern cannot be null or empty");
    }
  }
}
