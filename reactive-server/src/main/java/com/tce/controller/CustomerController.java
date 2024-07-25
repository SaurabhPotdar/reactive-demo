package com.tce.controller;

import com.tce.model.Customer;
import com.tce.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/customers")
    public Flux<Customer> getCustomers() {
        log.info("In Controller");
        return customerService.getCustomers();
    }

}
