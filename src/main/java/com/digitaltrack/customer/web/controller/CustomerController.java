package com.digitaltrack.customer.web.controller;

import com.digitaltrack.customer.model.dto.CustomerRegistrationRequest;
import com.digitaltrack.customer.web.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "customers")
@Slf4j
public record CustomerController(CustomerService customerService) {
    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
        log.info("start register new customer  {}", customerRegistrationRequest);
        customerService.register(customerRegistrationRequest);
        log.info("log id ");
    }
}
