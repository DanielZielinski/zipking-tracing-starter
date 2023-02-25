package com.daniel.zielinski.zipkin.config;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

public class ZipkinSpanHandlerDecorator extends SpanHandler {

    private final SpanHandler spanHandler;
    private final Set<String> notAllowedSpanNames;

    public ZipkinSpanHandlerDecorator(SpanHandler spanHandler, Set<String> notAllowedSpanNames) {
        this.spanHandler = spanHandler;
        this.notAllowedSpanNames = notAllowedSpanNames;
    }

    @Override
    public boolean begin(TraceContext context, MutableSpan span, TraceContext parent) {
        return spanHandler.begin(context, span, parent);
    }

    @Override
    public boolean end(TraceContext context, MutableSpan span, Cause cause) {
        if (containsNotAllowedName(span)) {
            return false;
        }
        return spanHandler.end(context, span, cause);
    }

    @Override
    public boolean handlesAbandoned() {
        return spanHandler.handlesAbandoned();
    }

    private boolean containsNotAllowedName(MutableSpan span) {
        if (CollectionUtils.isEmpty(notAllowedSpanNames)) {
            return false;
        }
        return notAllowedSpanNames.stream()
                .anyMatch(notAllowedSpanName -> notAllowedSpanName.equalsIgnoreCase(span.name()));
    }
}
