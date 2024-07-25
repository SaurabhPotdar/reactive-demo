package com.tce.service;

import com.tce.model.Customer;
import com.tce.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Flux<Customer> getCustomers() {
        log.info("In service");
        return customerRepository.findAll();
    }

}
