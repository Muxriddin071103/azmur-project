package uz.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.app.entity.User;
import uz.app.util.UserUtil;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final UserUtil userUtil;

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        Optional<User> currentUser = userUtil.getCurrentUser();

        if (currentUser.isPresent()) {
            User user = currentUser.get();
            String fullName = userUtil.getCurrentUserFullName();
            return ResponseEntity.ok(new DashboardResponse(user, fullName));
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    private record DashboardResponse(User user, String fullName) {
    }
}
