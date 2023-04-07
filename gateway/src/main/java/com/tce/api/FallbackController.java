package com.tce.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.LinkedHashSet;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FallbackController {

    @GetMapping("/fallback")
    public ResponseEntity<ErrorResponse> fallback(final ServerWebExchange exchange) {
        final LinkedHashSet<URI> set = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        final String originalUrl = set == null ? "Service unavailable" : set.toString();
        log.error("Service unavailable >>>> {}", originalUrl);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), originalUrl));
    }

}
