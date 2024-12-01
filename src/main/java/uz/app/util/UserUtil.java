package uz.app.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import uz.app.entity.User;
import uz.app.service.UserService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private final UserService userService;

    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User) {
            return Optional.of((User) principal);
        } else {
            return Optional.empty();
        }
    }

    public String getCurrentUserFullName() {
        Optional<User> currentUser = getCurrentUser();
        return currentUser.map(user -> user.getFirstName() + " " + user.getLastName()).orElse("Guest");
    }
}
