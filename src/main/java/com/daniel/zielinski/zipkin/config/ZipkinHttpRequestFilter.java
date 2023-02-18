package com.daniel.zielinski.zipkin.config;

import brave.SpanCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.zipkin", name = "enabled", havingValue = "true")
class ZipkinHttpRequestFilter implements Filter {

    private final ZipkinProperties zipkinProperties;
    private final SpanCustomizer spanCustomizer;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws ServletException, IOException {
        if (canAddTag(servletRequest)) {
            zipkinProperties.getCustomTags()
                    .forEach(zipkinCustomTag -> addTag(zipkinCustomTag, (HttpServletRequest) servletRequest));
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void addTag(ZipkinCustomTag zipkinCustomTag, HttpServletRequest httpRequest) {
        Optional.ofNullable(httpRequest.getHeader(zipkinCustomTag.getHeaderName()))
                .ifPresent(headerValue -> spanCustomizer.tag(zipkinCustomTag.getTagName(), headerValue));
    }

    private boolean canAddTag(ServletRequest servletRequest) {
        return !CollectionUtils.isEmpty(zipkinProperties.getCustomTags()) && servletRequest instanceof HttpServletRequest;
    }

}
