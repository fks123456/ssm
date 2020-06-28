package com.fks.service.impl;

import com.fks.dao.IUserDao;
import com.fks.domain.User;
import com.fks.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao userDao;

    @Override
    public List<User> findAllUserWithRole() {
        return userDao.findAllWithRole();
    }

    @Override
    public List<User> findAllUser() {
        return userDao.findAll();
    }

    @Override
    public User findUserByName(String username) {
        return userDao.findByName(username);
    }

    @Override
    public boolean save(User user) {
        return userDao.save(user) > 0;
    }
}
