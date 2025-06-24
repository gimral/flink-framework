package com.gimral.streaming.logger;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.layout.JsonTemplateLayout;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import java.nio.charset.Charset;
import java.util.Map;

@Plugin(name = "LeapJsonLayout", category = "Core", elementType = "layout", printObject = true)
class LeapJsonLayout extends JsonTemplateLayout {
    private final SensitiveDataMasker masker;

    protected LeapJsonLayout(Charset charset) {
        this(charset, SensitiveDataMasker.DEFAULT_PATTERNS, SensitiveDataMasker.DEFAULT_MASK);
    }

    public LeapJsonLayout(Charset charset, java.util.List<java.util.regex.Pattern> sensitivePatterns, String mask) {
        super(JsonTemplateLayout.newBuilder()
                .setCharset(charset)
                .setEventTemplate(
                        "{\"timestamp\":${json:timestamp},\"level\":${json:level},\"loggerName\":${json:loggerName},\"threadName\":${json:threadName},\"message\":${maskedMessage},\"thrown\":${maskedThrown}}\n")
                .setResolverProviders(
                        new TemplateResolverProvider[] { new MaskedResolverProvider(sensitivePatterns, mask) })
                .build());
        this.masker = new SensitiveDataMasker(sensitivePatterns, mask);
    }

    @PluginFactory
    public static LeapJsonLayout createLayout(
            @PluginAttribute(value = "mask", defaultString = SensitiveDataMasker.DEFAULT_MASK) String mask,
            @PluginAttribute(value = "charset", defaultString = "UTF-8") Charset charset) {
        return new LeapJsonLayout(charset, SensitiveDataMasker.DEFAULT_PATTERNS, mask);
    }

    // Custom resolver provider for masking
    static class MaskedResolverProvider implements TemplateResolver {
        private final SensitiveDataMasker masker;

        MaskedResolverProvider(java.util.List<java.util.regex.Pattern> patterns, String mask) {
            this.masker = new SensitiveDataMasker(patterns, mask);
        }

        @Override
        public void provideResolvers(TemplateResolverConfig config, TemplateResolvers.Builder builder) {
            builder.add("maskedMessage", new Resolver<LogEvent>() {
                @Override
                public Object resolve(LogEvent event, ResolverContext ctx) {
                    return masker.mask(event.getMessage().getFormattedMessage());
                }
            });
            builder.add("maskedThrown", new Resolver<LogEvent>() {
                @Override
                public Object resolve(LogEvent event, ResolverContext ctx) {
                    return event.getThrown() != null ? masker.mask(event.getThrown().toString()) : null;
                }
            });
        }
    }
}
