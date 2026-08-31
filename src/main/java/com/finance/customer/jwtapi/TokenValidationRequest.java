

import jakarta.validation.constraints.NotBlank;
public class TokenValidationRequest {

    @NotBlank(message = "token is required")
    private String token;

    // No-argument constructor
    public TokenValidationRequest() {
    }

    // All-argument constructor
    public TokenValidationRequest(String token) {
        this.token = token;
    }

    // Getter
    public String getToken() {
        return token;
    }

    // Setter
    public void setToken(String token) {
        this.token = token;
    }
}