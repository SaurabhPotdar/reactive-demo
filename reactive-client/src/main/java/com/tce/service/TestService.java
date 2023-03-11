package com.tce.service;

import com.tce.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@Configuration
public class TestService {

    private final String serverUrl;

    private final WebClient webClient = WebClient.builder().build();

    public TestService(@Value("${server.url}") String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Flux<Employee> get() {
        log.info("Get employees");
        return webClient.get()
                .uri(serverUrl + "/employees")
                .retrieve()
                .bodyToFlux(Employee.class)
                .doOnComplete(() -> log.info("Received response from server"));
    }

}
