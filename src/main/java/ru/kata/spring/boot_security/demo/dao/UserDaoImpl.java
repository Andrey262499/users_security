package ru.kata.spring.boot_security.demo.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from User", User.class).getResultList();
    }

    @Transactional
    @Override
    public void save(User user) {
        sessionFactory.getCurrentSession().merge(user);
    }

    @Transactional
    @Override
    public void delete(int id) {
        User user = sessionFactory.getCurrentSession().get(User.class, id);
        if (user != null) {
            sessionFactory.getCurrentSession().delete(user);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(int id) {
        return sessionFactory.getCurrentSession().get(User.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByEmail(String email) {
        return sessionFactory.getCurrentSession()
                .createQuery("from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .uniqueResult();
    }
}
