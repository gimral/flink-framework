package com.gimral.streaming.logger;

import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.layout.template.json.JsonTemplateLayout;

public class LeapLogManager {
    private static final String JSON_TEMPLATE = """
        {
            "timestamp": "$date{ISO8601}",
            "level": "%level",
            "logger": "%logger",
            "message": "%mask",
            "thread": "%thread",
            "context": {
                "mdc": "%mdc"
            },
            "exception": {
                "class": "%ex{className}",
                "message": "%ex{message}",
                "stacktrace": "%ex{full}"
            }
        }
        """;
    public static Logger getLogger(Class<?> clazz) {
        Logger logger = LogManager.getLogger(clazz);
        applyLayout(logger);
        return logger;
    }

    public static Logger getLogger(String name) {
        Logger logger = LogManager.getLogger(name);
        applyLayout(logger);
        return logger;
    }

    private static void applyLayout(Logger logger) {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        // Remove existing ConsoleAppender if present
        Appender oldConsole = config.getAppender("Console");
        if (oldConsole != null) {
            oldConsole.stop();
            config.getRootLogger().removeAppender("Console");
        }
        // Create LeapJsonLayout
        Layout<?> layout = JsonTemplateLayout.newBuilder()
                .setEventTemplate(JSON_TEMPLATE)
                .setStackTraceEnabled(true)
                .build();
        Appender appender = ConsoleAppender.newBuilder()
                .setName("Console")
                .setLayout(layout)
                .setTarget(ConsoleAppender.Target.SYSTEM_OUT)
                .build();
        appender.start();
        config.addAppender(appender);
        AppenderRef ref = AppenderRef.createAppenderRef("Console", null, null);
        AppenderRef[] refs = new AppenderRef[] { ref };
        LoggerConfig loggerConfig = config.getLoggerConfig(logger.getName());
        loggerConfig.addAppender(appender, null, null);
        ctx.updateLoggers();
    }
}
