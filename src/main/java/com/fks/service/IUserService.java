package com.fks.service;

import com.fks.domain.User;

import java.util.List;

public interface IUserService {
    public List<User> findAllUserWithRole();

    public List<User> findAllUser();

    public User findUserByName(String username);

    public boolean save(User user);
}
