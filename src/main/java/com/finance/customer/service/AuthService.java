

import com.example.smartCollege.dto.LoginRequest;
import com.example.smartCollege.dto.RegisterRequest;
import com.example.smartCollege.dto.AuthResponse;
import com.example.smartCollege.entity.Role;
import com.example.smartCollege.entity.User;
import com.example.smartCollege.exception.InvalidCredentialsException;
import com.example.smartCollege.exception.UserAlreadyExistsException;
import com.example.smartCollege.jwtapi.JwtService;
import com.example.smartCollege.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // ---------------------------------------------------------
    // REGISTER
    // ---------------------------------------------------------

    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {

            throw new UserAlreadyExistsException(
                    "Username '" + request.getUsername()
                            + "' is already taken"
            );
        }

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new UserAlreadyExistsException(
                    "Email '" + request.getEmail()
                            + "' is already registered"
            );
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        user.setAccountNonLocked(true);

        userRepository.save(user);

        return buildAuthResponse(user);
    }

    // ---------------------------------------------------------
    // LOGIN
    // ---------------------------------------------------------

    public AuthResponse login(LoginRequest request) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

        } catch (BadCredentialsException e) {

            throw new InvalidCredentialsException(
                    "Invalid username or password"
            );
        }

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid username or password"
                        )
                );

        return buildAuthResponse(user);
    }

    // ---------------------------------------------------------
    // BUILD AUTH RESPONSE
    // ---------------------------------------------------------

    private AuthResponse buildAuthResponse(User user) {

        String accessToken =
                jwtService.generateAccessToken(user);

        String refreshToken =
                jwtService.generateRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwtService.getAccessTokenExpirationSeconds(),
                user.getUsername(),
                user.getRole().name()
        );
    }
}