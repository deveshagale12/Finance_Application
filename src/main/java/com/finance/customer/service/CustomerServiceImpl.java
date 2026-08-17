package com.finance.customer.service;

import com.finance.customer.dto.CustomerRegistrationRequest;
import com.finance.customer.dto.CustomerResponse;
import com.finance.customer.entity.Customer;
import com.finance.customer.exception.CustomerAlreadyExistsException;
import com.finance.customer.repository.CustomerRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(
            CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public CustomerResponse registerCustomer(
            CustomerRegistrationRequest request) {

        validateDuplicateCustomer(request);

        Customer customer = new Customer();

        customer.setCustomerNumber(generateCustomerNumber());

        customer.setFirstName(
                request.getFirstName().trim()
        );

        customer.setMiddleName(
                request.getMiddleName() != null
                        ? request.getMiddleName().trim()
                        : null
        );

        customer.setLastName(
                request.getLastName().trim()
        );

        customer.setDateOfBirth(
                request.getDateOfBirth()
        );

        customer.setGender(
                request.getGender()
        );

        customer.setEmail(
                request.getEmail().trim().toLowerCase()
        );

        customer.setPhoneNumber(
                request.getPhoneNumber().trim()
        );

        customer.setCustomerType(
                request.getCustomerType()
        );

        customer.setNationality(
                request.getNationality() != null
                        ? request.getNationality().trim()
                        : null
        );

        Customer savedCustomer =
                customerRepository.save(customer);

        return mapToResponse(savedCustomer);
    }

    private void validateDuplicateCustomer(
            CustomerRegistrationRequest request) {

        String email =
                request.getEmail().trim().toLowerCase();

        if (customerRepository.existsByEmailIgnoreCase(email)) {

            throw new CustomerAlreadyExistsException(
                    "Customer already exists with email: " + email
            );
        }

        if (customerRepository.existsByPhoneNumber(
                request.getPhoneNumber().trim())) {

            throw new CustomerAlreadyExistsException(
                    "Customer already exists with phone number"
            );
        }
    }

    private String generateCustomerNumber() {

        return "CUST-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 12)
                        .toUpperCase();
    }

    private CustomerResponse mapToResponse(
            Customer customer) {

        CustomerResponse response =
                new CustomerResponse();

        response.setId(customer.getId());
        response.setCustomerNumber(
                customer.getCustomerNumber()
        );
        response.setFirstName(
                customer.getFirstName()
        );
        response.setMiddleName(
                customer.getMiddleName()
        );
        response.setLastName(
                customer.getLastName()
        );
        response.setDateOfBirth(
                customer.getDateOfBirth()
        );
        response.setGender(
                customer.getGender().name()
        );
        response.setEmail(
                customer.getEmail()
        );
        response.setPhoneNumber(
                customer.getPhoneNumber()
        );
        response.setCustomerType(
                customer.getCustomerType().name()
        );
        response.setNationality(
                customer.getNationality()
        );
        response.setStatus(
                customer.getStatus()
        );
        response.setCreatedAt(
                customer.getCreatedAt()
        );

        return response;
    }
}