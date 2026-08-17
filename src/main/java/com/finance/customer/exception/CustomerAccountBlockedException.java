package com.finance.customer.exception;

public class CustomerAccountBlockedException extends RuntimeException {

    public CustomerAccountBlockedException(String message) {
        super(message);
    }
}