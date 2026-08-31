
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Example of an endpoint that requires BOTH a valid API key (ApiKeyAuthFilter)
 * AND a valid JWT (JwtAuthFilter) to access, demonstrating how the two
 * security mechanisms link together.
 */

@RestController
@RequestMapping("/api/secure")
public class SecureController {

    @GetMapping("/me")
    public Map<String, Object> whoAmI(Authentication authentication) {
        return Map.of(
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities()
        );
    }
}
