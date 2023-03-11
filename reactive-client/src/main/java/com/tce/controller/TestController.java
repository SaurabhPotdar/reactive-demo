package com.tce.controller;

import com.tce.model.Employee;
import com.tce.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/employees")
    public Flux<Employee> get() {
        log.info("In Controller");
        return testService.get();
    }

}