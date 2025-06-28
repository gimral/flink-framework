package com.gimral.streaming.logger;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.layout.template.json.resolver.EventResolver;
import org.apache.logging.log4j.layout.template.json.resolver.TemplateResolverConfig;
import org.apache.logging.log4j.layout.template.json.util.JsonWriter;

public class MaskingResolver implements EventResolver {
  private final SensitiveDataPatternMasker sensitiveDataPatternMasker;

  MaskingResolver(final TemplateResolverConfig config) {
    String patternDefinitionPath = config.getString("patterns");
    sensitiveDataPatternMasker = new SensitiveDataPatternMasker(patternDefinitionPath);
  }

  static String getName() {
    return "mask";
  }

  @Override
  public void resolve(LogEvent value, JsonWriter jsonWriter) {
    String masked = sensitiveDataPatternMasker.mask(value.getMessage().getFormattedMessage());
    jsonWriter.writeString(masked);
  }
}
