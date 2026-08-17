package com.finance.customer.service;
import com.Finance.customer.dto.*;
import com.Finance.customer.entity.Customer;
import com.Finance.customer.entity.CustomerStatus;
import com.Finance.customer.exception.*;
import com.Finance.customer.repository.CustomerRepository;
import com.Finance.customer.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CustomerServiceImpl
        implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public CustomerServiceImpl(
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public CustomerResponse registerCustomer(
            CustomerRegistrationRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        String phoneNumber = request.getPhoneNumber()
                .trim();

        if (customerRepository.existsByEmailIgnoreCase(email)) {

            throw new CustomerAlreadyExistsException(
                    "Customer already exists with email"
            );
        }

        if (customerRepository.existsByPhoneNumber(phoneNumber)) {

            throw new CustomerAlreadyExistsException(
                    "Customer already exists with phone number"
            );
        }

        Customer customer = new Customer();

        customer.setCustomerNumber(
                generateCustomerNumber()
        );

        customer.setFirstName(
                request.getFirstName().trim()
        );

        if (request.getMiddleName() != null
                && !request.getMiddleName().isBlank()) {

            customer.setMiddleName(
                    request.getMiddleName().trim()
            );
        }

        customer.setLastName(
                request.getLastName().trim()
        );

        customer.setDateOfBirth(
                request.getDateOfBirth()
        );

        customer.setGender(
                request.getGender()
        );

        customer.setEmail(email);

        customer.setPhoneNumber(phoneNumber);

        /*
         * NEVER store the raw password.
         */
        customer.setPasswordHash(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        customer.setCustomerType(
                request.getCustomerType()
        );

        if (request.getNationality() != null) {

            customer.setNationality(
                    request.getNationality().trim()
            );
        }

        Customer savedCustomer =
                customerRepository.save(customer);

        return mapToResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerLoginResponse login(
            CustomerLoginRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        /*
         * Don't reveal whether the email exists.
         */
        Customer customer =
                customerRepository
                        .findByEmailIgnoreCase(email)
                        .orElseThrow(() ->
                                new InvalidCredentialsException(
                                        "Invalid email or password"
                                )
                        );

        validateCustomerAccount(customer);

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        customer.getPasswordHash()
                );

        if (!passwordMatches) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        String token =
                jwtService.generateToken(
                        customer.getId(),
                        customer.getEmail()
                );

        return new CustomerLoginResponse(
                token,
                "Bearer",
                customer.getId(),
                customer.getCustomerNumber(),
                customer.getEmail()
        );
    }

    private void validateCustomerAccount(
            Customer customer) {

        CustomerStatus status =
                customer.getStatus();

        if (status == CustomerStatus.BLOCKED) {

            throw new CustomerAccountBlockedException(
                    "Customer account is blocked"
            );
        }

        if (status == CustomerStatus.SUSPENDED) {

            throw new CustomerAccountSuspendedException(
                    "Customer account is suspended"
            );
        }

        if (status == CustomerStatus.CLOSED) {

            throw new CustomerAccountClosedException(
                    "Customer account is closed"
            );
        }

        if (status != CustomerStatus.ACTIVE) {

            throw new CustomerAccountNotActiveException(
                    "Customer account is not active"
            );
        }
    }

    private String generateCustomerNumber() {

        return "CUST-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
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