CREATE TABLE IF NOT EXISTS public.customers (
    id UUID PRIMARY KEY,

    customer_number VARCHAR(30) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,

    date_of_birth DATE NOT NULL,

    gender VARCHAR(20) NOT NULL,

    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,

    customer_type VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,

    nationality VARCHAR(50),

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    version BIGINT,

    CONSTRAINT uk_customer_number
        UNIQUE (customer_number),

    CONSTRAINT uk_customer_email
        UNIQUE (email),

    CONSTRAINT uk_customer_phone
        UNIQUE (phone_number)
);