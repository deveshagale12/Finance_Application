package com.finance.customer.service;

import com.Finance.customer.dto.CustomerLoginRequest;
import com.Finance.customer.dto.CustomerLoginResponse;
import com.Finance.customer.dto.CustomerRegistrationRequest;
import com.Finance.customer.dto.CustomerResponse;

public interface CustomerService {

    CustomerResponse registerCustomer(
            CustomerRegistrationRequest request
    );

    CustomerLoginResponse login(
            CustomerLoginRequest request
    );
}