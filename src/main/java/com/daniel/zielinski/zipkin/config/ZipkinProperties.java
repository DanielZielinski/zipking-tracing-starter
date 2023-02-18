package com.daniel.zielinski.zipkin.config;


import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "spring.zipkin")
@ConditionalOnProperty(prefix = "spring.zipkin", name = "enabled", havingValue = "true")
class ZipkinProperties {

    private List<ZipkinCustomTag> customTags = new ArrayList<>();

    @NotNull
    private ZipkinRabbitProperties rabbitmq;

    @Data
    public static class ZipkinRabbitProperties {

        @NotBlank
        private String addresses;

        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

}
