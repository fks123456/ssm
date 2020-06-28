package com.fks.dao;

import com.fks.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IRoleDao {
    public List<Role> findAllWithRes();

    public List<Role> findAll();

    public Role findById(Integer id);

    public Role findByIdWithRes(Integer id);
}
