package com.finance.customer.dto;

import java.util.UUID;

public class CustomerLoginResponse {

    private String accessToken;
    private String tokenType;
    private UUID customerId;
    private String customerNumber;
    private String email;

    public CustomerLoginResponse() {
    }

    public CustomerLoginResponse(
            String accessToken,
            String tokenType,
            UUID customerId,
            String customerNumber,
            String email) {

        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.customerId = customerId;
        this.customerNumber = customerNumber;
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}