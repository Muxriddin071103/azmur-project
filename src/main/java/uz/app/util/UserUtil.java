package uz.app.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import uz.app.entity.User;
import uz.app.repository.UserRepository;
import uz.app.service.UserService;

import java.util.Optional;

@Component
public class UserUtil {

    private final UserService userService;
    public UserUtil(UserService userService, UserRepository userRepository) {
        this.userService = userService;
    }

    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userService.findByUsername(username);
        } else {
            return Optional.empty();
        }
    }

    public String getCurrentUserFullName() {
        Optional<User> currentUser = getCurrentUser();
        return currentUser.map(user -> user.getFirstName() + " " + user.getLastName()).orElse("Guest");
    }
}
