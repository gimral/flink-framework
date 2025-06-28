package com.gimral.streaming.logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.layout.template.json.util.Uris;

class SensitiveDataPatternMasker {
  private final List<MaskingPattern> maskingPatterns;

  public SensitiveDataPatternMasker(String patternDefinitionPath) {
    maskingPatterns = loadMaskingPatterns(patternDefinitionPath);
  }

  public List<MaskingPattern> loadMaskingPatterns(String patternDefinitionPath) {
    if (patternDefinitionPath == null || patternDefinitionPath.trim().isEmpty()) {
      throw new IllegalStateException("Masking patterns path is not defined.");
    }
    String definitions = Uris.readUri(patternDefinitionPath, StandardCharsets.UTF_8);
    ObjectMapper objectMapper = new ObjectMapper();
    List<MaskingPattern> maskingPatterns;
    try {
      maskingPatterns = objectMapper.readValue(definitions, new TypeReference<>() {});
    } catch (Exception e) {
      throw new IllegalStateException("Failed to parse masking patterns JSON", e);
    }
    if (maskingPatterns.isEmpty()) {
      throw new IllegalStateException("No masking patterns found in JSON configuration");
    }
    return Collections.unmodifiableList(maskingPatterns);
  }

  public String mask(String input) {
    String masked = input;
    for (MaskingPattern maskingPattern : maskingPatterns) {
      try {
        Pattern pattern = maskingPattern.getPattern();
        Matcher matcher = pattern.matcher(masked);
        masked = matcher.replaceAll(maskingPattern.getReplacement());
      } catch (Exception e) {
        // Log error and continue with next pattern to prevent one bad pattern from
        // breaking masking
        // TODO: Add proper logging here
      }
    }
    return masked;
  }
}
