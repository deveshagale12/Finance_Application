package com.finance.jwtapi;


import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.finance.jwtapi.JwtService;


@RestController
@RequestMapping("/api/jwt")
public class JwtController {

    private final JwtService jwtService;

    // Manual constructor - replaces @RequiredArgsConstructor
    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validate(
            @Valid @RequestBody TokenValidationRequest request) {

        String username = jwtService.extractUsername(
                request.getToken()
        );

        java.util.Date expiry = jwtService.extractExpiration(
                request.getToken()
        );

        boolean valid = expiry.after(new java.util.Date());

        // Manual object creation - replaces TokenValidationResponse.builder()
        TokenValidationResponse response = new TokenValidationResponse(
                valid,
                username,
                expiry
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/claims")
    public ResponseEntity<Claims> claims(
            @RequestParam String token) {

        Claims claims = jwtService.extractClaim(
                token,
                c -> c
        );

        return ResponseEntity.ok(claims);
    }
}