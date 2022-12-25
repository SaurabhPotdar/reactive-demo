package com.tce.service;

import com.tce.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class TestService {

    @Autowired
    private WebClient webClient;

    public Flux<Employee> get() {
        return this.webClient.get()
                .uri("/employees")
                .retrieve()
                .bodyToFlux(Employee.class)
                .doOnComplete(() -> log.info("Received response from server"));
    }

}
