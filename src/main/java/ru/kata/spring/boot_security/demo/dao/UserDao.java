package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    List<User> findAll();
    void save(User user);
    void delete(int id);
    User findById(int id);

    User findByEmail(String email);
}
