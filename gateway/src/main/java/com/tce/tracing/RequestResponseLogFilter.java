package com.tce.tracing;

import brave.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestResponseLogFilter implements GlobalFilter, Ordered {

    private final Tracer tracer;

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        addTraceIdResponseHeader(exchange);
        final ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 1;
    }

    private void addTraceIdResponseHeader(final ServerWebExchange exchange) {
        final String traceId = tracer.currentSpan().context().traceIdString();
        log.info("Adding traceId to response header: {}", traceId);
        exchange.getResponse().getHeaders().add("X-B3-RequestId", traceId);
    }

}

