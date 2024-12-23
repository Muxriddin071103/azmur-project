package uz.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.app.config.JwtProvider;
import uz.app.entity.User;
import uz.app.entity.enums.Role;
import uz.app.payload.AuthRequest;
import uz.app.payload.LoginRequest;
import uz.app.payload.SignUpDTO;
import uz.app.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public User registerUser(SignUpDTO signUpDTO) {
        if (userRepository.existsByUsername(signUpDTO.getUsername())) {
            throw new RuntimeException("Username is already in use");
        }

        User user = User.builder()
                .firstName(signUpDTO.getFirstName())
                .lastName(signUpDTO.getLastName())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .username(signUpDTO.getUsername())
                .age(signUpDTO.getAge())
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .enabled(false)
                .build();

        return userRepository.save(user);
    }

    public AuthRequest authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            user.setEnabled(true);
            userRepository.save(user);
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String accessToken = jwtProvider.generateToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        return new AuthRequest(accessToken, refreshToken);
    }

    public User getUserFromAccessToken(String accessToken) {
        if (!jwtProvider.validateToken(accessToken)) {
            throw new RuntimeException("Invalid or expired access token");
        }

        String username = jwtProvider.getSubject(accessToken);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
