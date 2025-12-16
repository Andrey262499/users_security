package ru.kata.spring.boot_security.demo.dao;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    private final SessionFactory sessionFactory;

    public RoleDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Role> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from Role", Role.class).getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public Role findById(Long id) {
        return sessionFactory.getCurrentSession().get(Role.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public Role findByName(String name) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Role r where r.name = :name", Role.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Transactional
    @Override
    public void save(Role role) {
        sessionFactory.getCurrentSession().merge(role);
    }
}
