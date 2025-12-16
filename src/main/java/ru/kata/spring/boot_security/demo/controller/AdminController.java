package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("newUser", new User());
        model.addAttribute("updateUser", new User());
        return "admin";
    }

    @PostMapping
    public String save(@ModelAttribute("newUser") User user,
                       @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        Set<Role> roles = getRoles(roleIds);

        if (roles.isEmpty()) {
            Role defaultRole = roleService.findByName("ROLE_USER");

            if (defaultRole != null) {
                roles.add(defaultRole);
            }
        }

        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("updateUser") User user,
                         @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        user.setRoles(getRoles(roleIds));
        userService.update(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam int id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    private Set<Role> getRoles(List<Long> roleIds) {
        return Optional.ofNullable(roleIds)
                       .orElse(new ArrayList<>(0))
                       .stream()
                       .map(roleService::findById)
                       .filter(Objects::nonNull)
                       .collect(Collectors.toSet());
    }
}
