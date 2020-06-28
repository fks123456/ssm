package com.fks.dao;

import com.fks.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface IUserDao {

    // 查询用户所有属性包括角色信息
    public List<User> findAllWithRole();

    // 查询用户基本属性
    public List<User> findAll();

    public User findByName(String username);

    public int save(User user);
}
