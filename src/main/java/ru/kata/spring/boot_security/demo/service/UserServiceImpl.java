package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDaoImpl;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserDaoImpl userDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public UserServiceImpl(UserDaoImpl userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByLogin(String login) {
        return userDao.getUserByLogin(login);
    }



    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        User user = userDao.getUserByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", login));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRoleToAuthorities(user.getRoles()));
    }



    private Collection<? extends GrantedAuthority> mapRoleToAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
    }

    @Override
    public List<User> getAllUsers() {
            return userDao.getUsers();
    }

    @Override
    public User getUserById(long id) {
            return userDao.getUserById(id);
    }

    @Override
    public User delete(long id) {
        User user = null;
        try {
            user = userDao.deleteUser(id);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void create(User user)  {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDao.createUser(user);
    }

    @Override
    public void update(int id, User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.updateUser(user);
    }
}



