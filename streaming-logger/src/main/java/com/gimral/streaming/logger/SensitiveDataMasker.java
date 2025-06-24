package com.gimral.streaming.logger;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Arrays;
import java.util.Collections;

class SensitiveDataMasker {
    public static final String DEFAULT_MASK = "***";
    public static final List<Pattern> DEFAULT_PATTERNS = Collections.unmodifiableList(Arrays.asList(
            Pattern.compile("(?i)password\\s*[:=]\\s*\\S+"),
            Pattern.compile("(?i)secret\\s*[:=]\\s*\\S+"),
            Pattern.compile("(?i)token\\s*[:=]\\s*\\S+")));

    private final List<Pattern> sensitivePatterns;
    private final String mask;

    public SensitiveDataMasker() {
        this(DEFAULT_PATTERNS, DEFAULT_MASK);
    }

    public SensitiveDataMasker(List<Pattern> sensitivePatterns, String mask) {
        this.sensitivePatterns = sensitivePatterns;
        this.mask = mask;
    }

    public String mask(String input) {
        String masked = input;
        for (Pattern pattern : sensitivePatterns) {
            Matcher matcher = pattern.matcher(masked);
            masked = matcher.replaceAll(mask);
        }
        return masked;
    }
}
