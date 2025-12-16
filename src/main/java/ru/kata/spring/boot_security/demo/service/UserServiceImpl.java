package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public void save(User user) {
        user.setPassword(encodeIfNeeded(user.getPassword()));
        userDao.save(user);
    }

    @Override
    public void update(User user) {
        User nowUser = userDao.findById(user.getId());
        if (nowUser == null) {
            return;
        }

        nowUser.setName(user.getName());
        nowUser.setEmail(user.getEmail());

        // пароль обновляем только если он задан
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            nowUser.setPassword(encodeIfNeeded(user.getPassword()));
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            nowUser.setRoles(user.getRoles());
        }

        userDao.save(nowUser);
    }

    @Override
    public void delete(int id) {
        userDao.delete(id);
    }

    private String encodeIfNeeded(String rawOrEncoded) {
        if (rawOrEncoded == null || rawOrEncoded.isBlank()) {
            return rawOrEncoded;
        }
        // простая защита от повторного BCrypt-энкода
        if (rawOrEncoded.startsWith("$2a$") || rawOrEncoded.startsWith("$2b$") || rawOrEncoded.startsWith("$2y$")) {
            return rawOrEncoded;
        }
        return passwordEncoder.encode(rawOrEncoded);
    }
}
