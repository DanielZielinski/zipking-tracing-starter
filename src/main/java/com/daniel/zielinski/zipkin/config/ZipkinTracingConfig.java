package com.daniel.zielinski.zipkin.config;

import brave.Tracing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.reporter.Sender;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.zipkin", name = "enabled", havingValue = "true")
class ZipkinTracingConfig {

    private final ZipkinProperties zipkinProperties;

    private final Sender sender;

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public Tracing tracing() {
        log.info("Registering zipkin tracing");
        AsyncZipkinSpanHandler spanHandler = AsyncZipkinSpanHandler
                .newBuilder(sender)
                .build();
        ZipkinSpanHandlerDecorator zipkinSpanHandlerDecorator = new ZipkinSpanHandlerDecorator(spanHandler,
                zipkinProperties.getNotAllowedSpanNames());
        return Tracing.newBuilder()
                .localServiceName(applicationName)
                .addSpanHandler(zipkinSpanHandlerDecorator)
                .build();
    }
}
