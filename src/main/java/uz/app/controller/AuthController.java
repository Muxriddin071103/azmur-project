package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.User;
import uz.app.payload.LoginRequest;
import uz.app.payload.SignUpDTO;
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
    public ResponseEntity<String> signIn(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("Invalid credentials or account not activated");
        }
    }
}
