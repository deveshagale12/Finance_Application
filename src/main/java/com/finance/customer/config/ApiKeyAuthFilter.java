

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.example.smartCollege.exception.InvalidApiKeyException;
import com.example.smartCollege.exception.MissingApiKeyException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Value("${api.security.header-name}")
    private String headerName;

    @Value("${api.security.valid-keys}")
    private String validKeysRaw;

    private final HandlerExceptionResolver resolver;

    public ApiKeyAuthFilter(
            @Qualifier("handlerExceptionResolver")
            HandlerExceptionResolver resolver) {

        this.resolver = resolver;
    }

    private static final Set<String> EXCLUDED_PATHS =
            new HashSet<>(Arrays.asList(

                "/",
                "/index.html",
                "/userdash.html",

                "/css/",
                "/js/",
                "/images/",
                "/favicon.ico",

                "/api/auth/register",
                "/api/auth/login",

                "/h2-console",
                "/actuator/health"
            ));

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path = request.getRequestURI();

        return EXCLUDED_PATHS.stream()
                .anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {

            String apiKey = request.getHeader(headerName);

            if (apiKey == null || apiKey.isBlank()) {

                throw new MissingApiKeyException(
                        "Missing required header: " + headerName
                );
            }

            boolean validKey = Arrays.stream(
                        validKeysRaw.split(",")
                    )
                    .map(String::trim)
                    .anyMatch(key -> key.equals(apiKey));

            if (!validKey) {

                throw new InvalidApiKeyException(
                        "The provided API key is invalid"
                );
            }

            filterChain.doFilter(request, response);

        } catch (MissingApiKeyException |
                 InvalidApiKeyException ex) {

            resolver.resolveException(
                    request,
                    response,
                    null,
                    ex
            );
        }
    }
}