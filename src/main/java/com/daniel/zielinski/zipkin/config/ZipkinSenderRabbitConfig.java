package com.daniel.zielinski.zipkin.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.zipkin2.ZipkinAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zipkin2.reporter.amqp.RabbitMQSender;

@Slf4j
@RequiredArgsConstructor
@Configuration
@ConditionalOnProperty(prefix = "spring.zipkin", name = "enabled", havingValue = "true")
class ZipkinSenderRabbitConfig {

    private final ZipkinProperties zipkinProperties;

    @Bean(ZipkinAutoConfiguration.SENDER_BEAN_NAME)
    public RabbitMQSender zipkinRabbitMqSender() {
        log.info("Registering zipkin sender");
        return RabbitMQSender.newBuilder()
                .addresses(zipkinProperties.getRabbitmq().getAddresses())
                .username(zipkinProperties.getRabbitmq().getUsername())
                .password(zipkinProperties.getRabbitmq().getPassword())
                .build();
    }
}
