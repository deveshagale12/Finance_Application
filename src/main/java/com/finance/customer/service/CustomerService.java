package com.finance.customer.service;

import com.finance.customer.dto.CustomerRegistrationRequest;
import com.finance.customer.dto.CustomerResponse;

public interface CustomerService {

    CustomerResponse registerCustomer(
            CustomerRegistrationRequest request
    );
}