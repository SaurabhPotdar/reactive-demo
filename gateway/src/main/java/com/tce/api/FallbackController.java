package com.tce.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FallbackController {

    @GetMapping("/fallback")
    public ResponseEntity<String> fallback(final ServerWebExchange exchange) {
        final String exception = ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR;
        log.debug("Exception {}", exception);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service unavailable");
    }

}
