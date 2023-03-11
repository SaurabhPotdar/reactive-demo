package com.tce.controller;

import com.tce.model.Employee;
import com.tce.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/employees")
    public Flux<Employee> getEmployees() {
        log.info("In Controller");
        return employeeService
                .getEmployees();
    }

}
