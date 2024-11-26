package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uz.app.util.UserUtil;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserUtil userUtil;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        userUtil.getCurrentUser().ifPresent(user -> model.addAttribute("user", user));
        String fullName = userUtil.getCurrentUserFullName();
        model.addAttribute("fullName", fullName);
        return "main-page";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/auth/sign-up";
    }
}
