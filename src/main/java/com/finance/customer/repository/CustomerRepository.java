package com.finance.customer.repository;

import com.finance.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository
        extends JpaRepository<Customer, UUID> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Customer> findByEmailIgnoreCase(String email);

        boolean existsByCustomerNumber(String customerNumber);
}