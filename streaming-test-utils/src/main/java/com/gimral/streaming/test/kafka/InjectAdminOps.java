package com.gimral.streaming.test.kafka;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a test method parameter with this annotation to inject the {@link io.github.embeddedkafka.ops.AdminOps} instance.
 *
 * @see EmbeddedKafkaExtension
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectAdminOps {}
