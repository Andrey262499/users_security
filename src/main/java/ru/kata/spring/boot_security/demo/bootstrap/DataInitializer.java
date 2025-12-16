package ru.kata.spring.boot_security.demo.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;

    public DataInitializer(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Role adminRole = getOrCreateRole("ROLE_ADMIN");
        Role userRole = getOrCreateRole("ROLE_USER");

        checkUser("Admin", "admin@mail.ru", "admin", Set.of(adminRole));
        checkUser("User", "user@mail.ru", "user", Set.of(userRole));
    }

    private Role getOrCreateRole(String roleName) {
        Role role = roleService.findByName(roleName);

        if (role == null) {
            roleService.save(new Role(roleName));
            role = roleService.findByName(roleName);
        }

        if (role == null) {
            throw new IllegalStateException("Role invalid: " + roleName);
        }

        return role;
    }

    private void checkUser(String name, String email, String password, Set<Role> roles) {
        User existing = userService.findByEmail(email);

        if (existing != null) {
            if (existing.getRoles() == null || existing.getRoles().isEmpty()) {
                existing.setRoles(new HashSet<>(roles));
                userService.update(existing);
            }
            return;
        }

        User user = new User(name, email, password);
        user.setRoles(new HashSet<>(roles));
        userService.save(user);
    }
}
