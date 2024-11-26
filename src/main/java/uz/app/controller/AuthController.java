package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.app.entity.User;
import uz.app.payload.SignUpDTO;
import uz.app.payload.LoginRequest;
import uz.app.service.AuthService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/sign-up")
    public String showSignUpForm(Model model) {
        model.addAttribute("signUpDTO", new SignUpDTO());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute SignUpDTO signUpDTO, Model model) {
        User registeredUser = authService.registerUser(signUpDTO);
        model.addAttribute("user", registeredUser);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String signIn(@ModelAttribute LoginRequest loginRequest, Model model) {
        String token = authService.authenticateUser(loginRequest);
        model.addAttribute("token", token);
        return "main-page";
    }
}
