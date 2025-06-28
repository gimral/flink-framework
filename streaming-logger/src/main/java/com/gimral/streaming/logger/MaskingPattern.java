package com.gimral.streaming.logger;

import java.util.regex.Pattern;

class PatternDefinition {
    private final Pattern pattern;
    private final String replacement;

    public PatternDefinition(String pattern, String replacement) {
        validatePattern(pattern);
        this.pattern = Pattern.compile(pattern);
        this.replacement = replacement;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getReplacement() {
        return replacement;
    }

    private void validatePattern(String pattern) {
        if (pattern == null || pattern.trim().isEmpty()) {
            throw new IllegalArgumentException("Pattern cannot be null or empty");
        }
    }
}
