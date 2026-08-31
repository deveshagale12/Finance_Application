package com.finance.jwtapi;


import java.util.Date;

public class TokenValidationResponse {

    private boolean valid;

    private String username;

    private Date expiresAt;

    // No-argument constructor
    public TokenValidationResponse() {
    }

    // All-argument constructor
    public TokenValidationResponse(
            boolean valid,
            String username,
            Date expiresAt) {

        this.valid = valid;
        this.username = username;
        this.expiresAt = expiresAt;
    }

    // Getters

    public boolean isValid() {
        return valid;
    }

    public String getUsername() {
        return username;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    // Setters

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}