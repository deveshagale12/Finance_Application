package com.finance.jwtapi;


import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.finance.exception.InvalidJwtException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    // Manual constructor - replaces @RequiredArgsConstructor
    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    // ==========================================
    // Create signing key
    // ==========================================

    private SecretKey signingKey() {

        byte[] keyBytes = Base64.getDecoder()
                .decode(jwtProperties.getSecret());

        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ==========================================
    // Generate Access Token
    // ==========================================

    public String generateAccessToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();

        userDetails.getAuthorities()
                .forEach(a -> claims.put("role", a.getAuthority()));

        return buildToken(
                claims,
                userDetails.getUsername(),
                jwtProperties.getAccessTokenExpirationMs()
        );
    }

    // ==========================================
    // Generate Refresh Token
    // ==========================================

    public String generateRefreshToken(UserDetails userDetails) {

        return buildToken(
                Map.of("type", "refresh"),
                userDetails.getUsername(),
                jwtProperties.getRefreshTokenExpirationMs()
        );
    }

    // ==========================================
    // Build JWT Token
    // ==========================================

    private String buildToken(
            Map<String, Object> claims,
            String subject,
            long expirationMs) {

        Date now = new Date();

        Date expiry = new Date(
                now.getTime() + expirationMs
        );

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey())
                .compact();
    }

    // ==========================================
    // Extract Username
    // ==========================================

    public String extractUsername(String token) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }

    // ==========================================
    // Extract Expiration
    // ==========================================

    public Date extractExpiration(String token) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }

    // ==========================================
    // Extract Any Claim
    // ==========================================

    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    // ==========================================
    // Extract All Claims
    // ==========================================

    private Claims extractAllClaims(String token) {

        try {

            return Jwts.parser()
                    .verifyWith(signingKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (ExpiredJwtException
                | MalformedJwtException
                | SignatureException
                | UnsupportedJwtException e) {

            // Re-throw JJWT exceptions so
            // GlobalExceptionHandler can handle them.

            throw e;

        } catch (IllegalArgumentException e) {

            throw new InvalidJwtException(
                    "JWT token is empty or invalid",
                    e
            );
        }
    }

    // ==========================================
    // Validate Token
    // ==========================================

    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        final String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // ==========================================
    // Check Token Expiration
    // ==========================================

    private boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }

    // ==========================================
    // Access Token Expiration in Seconds
    // ==========================================

    public long getAccessTokenExpirationSeconds() {

        return jwtProperties.getAccessTokenExpirationMs() / 1000;
    }
}