
package com.finance.jwtapi;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;

    private long accessTokenExpirationMs;

    private long refreshTokenExpirationMs;

    private String issuer;

    // No-argument constructor
    public JwtProperties() {
    }

    // Getters

    public String getSecret() {
        return secret;
    }

    public long getAccessTokenExpirationMs() {
        return accessTokenExpirationMs;
    }

    public long getRefreshTokenExpirationMs() {
        return refreshTokenExpirationMs;
    }

    public String getIssuer() {
        return issuer;
    }

    // Setters

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setAccessTokenExpirationMs(long accessTokenExpirationMs) {
        this.accessTokenExpirationMs = accessTokenExpirationMs;
    }

    public void setRefreshTokenExpirationMs(long refreshTokenExpirationMs) {
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}