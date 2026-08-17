package com.finance.customer.controller;

import com.Finance.customer.dto.CustomerLoginRequest;
import com.Finance.customer.dto.CustomerLoginResponse;
import com.Finance.customer.dto.CustomerRegistrationRequest;
import com.Finance.customer.dto.CustomerResponse;
import com.Finance.customer.service.CustomerService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(
            CustomerService customerService) {

        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> registerCustomer(
            @Valid
            @RequestBody
            CustomerRegistrationRequest request) {

        CustomerResponse response =
                customerService.registerCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomerLoginResponse> login(
            @Valid
            @RequestBody
            CustomerLoginRequest request) {

        CustomerLoginResponse response =
                customerService.login(request);

        return ResponseEntity.ok(response);
    }
}