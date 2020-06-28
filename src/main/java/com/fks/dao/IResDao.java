package com.fks.dao;

import com.fks.domain.Res;
import com.fks.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IResDao {

    public List<Res> findAll();

    public List<Res> findByRoleId(Integer id);

    public Res findById(Integer id);

    public List<String> findAllResNameByRoleId(Integer role_id);
}
