package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDaoImpl;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private RoleDaoImpl rolesDao;

    public RoleServiceImpl(RoleDaoImpl rolesDao) {
        this.rolesDao = rolesDao;
    }

    @Override
    public List<Role> getRoles() {
        return rolesDao.findAll();
    }
}
