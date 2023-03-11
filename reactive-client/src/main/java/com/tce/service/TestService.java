package com.tce.service;

import com.tce.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@Configuration
public class TestService {

    private final String gatewayUrl;

    private final WebClient webClient = WebClient.builder().build();

    public TestService(@Value("${gateway.url}") String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public Flux<Employee> get() {
        log.info("Get employees");
        return webClient.get()
                .uri(gatewayUrl + "/server/employees")
                //Injecting current traceId and spanId into request header
                .header("X-B3-TraceId", MDC.get("traceId"))
                .header("X-B3-SpanId", MDC.get("spanId"))
                .retrieve()
                .bodyToFlux(Employee.class)
                .doOnComplete(() -> log.info("Received response from server"));
    }

}
