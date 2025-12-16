package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class UserPageController {

    private final UserService userService;

    public UserPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String userPage(Authentication authentication, Model model) {
        String email = authentication != null ? authentication.getName() : null;
        User user = (email != null) ? userService.findByEmail(email) : null;
        model.addAttribute("user", user);
        model.addAttribute("email", email);
        return "user";
    }
}
