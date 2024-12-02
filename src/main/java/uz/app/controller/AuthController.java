package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.User;
import uz.app.payload.AuthRequest;
import uz.app.payload.LoginRequest;
import uz.app.payload.SignUpDTO;
import uz.app.payload.RefreshTokenRequest;
import uz.app.service.AuthService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<User> signUp(@RequestBody SignUpDTO signUpDTO) {
        try {
            User registeredUser = authService.registerUser(signUpDTO);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthRequest> signIn(@RequestBody LoginRequest loginRequest) {
        try {
            AuthRequest authResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserFromToken(@RequestHeader(value = "Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body(null);
        }
        try {
            String token = authorizationHeader.substring(7);
            User user = authService.getUserFromAccessToken(token);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

}
