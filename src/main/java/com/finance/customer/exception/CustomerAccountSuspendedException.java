package com.finance.customer.exception;

public class CustomerAccountSuspendedException extends RuntimeException {

    public CustomerAccountSuspendedException(String message) {
        super(message);
    }
}