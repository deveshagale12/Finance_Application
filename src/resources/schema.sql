-- Step 1: Create a custom TYPE for the role ENUM in PostgreSQL
CREATE TYPE user_role AS ENUM ('ROLE_ADMIN', 'ROLE_USER');

-- Step 2: Create the app_user table
CREATE TABLE app_user (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role user_role NOT NULL DEFAULT 'ROLE_USER',
    account_non_locked BOOLEAN DEFAULT TRUE,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP(6)
);