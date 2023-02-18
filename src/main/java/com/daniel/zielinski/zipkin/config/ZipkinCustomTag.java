package com.daniel.zielinski.zipkin.config;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@Data
class ZipkinCustomTag {

    @NotBlank
    private String headerName;
    @NotBlank
    private String tagName;
}
