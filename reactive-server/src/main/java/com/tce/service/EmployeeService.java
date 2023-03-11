package com.tce.service;

import com.tce.model.Employee;
import com.tce.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Flux<Employee> getEmployees() {
        log.info("In service");
        return employeeRepository.findAll();
    }

}
