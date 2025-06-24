package com.gimral.streaming.logger;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.layout.template.json.resolver.*;

@Plugin(name = "MaskingResolverFactory", category = TemplateResolverFactory.CATEGORY)
public final class MaskingResolverFactory implements EventResolverFactory {

    private static final MaskingResolverFactory INSTANCE = new MaskingResolverFactory();

    private MaskingResolverFactory() {}

    @PluginFactory
    public static MaskingResolverFactory getInstance() {
        return INSTANCE;
    }
    @Override
    public String getName() {
        return MaskingResolver.getName();
    }

    @Override
    public TemplateResolver<LogEvent> create(EventResolverContext context, TemplateResolverConfig config) {
        return new MaskingResolver();
    }
}
