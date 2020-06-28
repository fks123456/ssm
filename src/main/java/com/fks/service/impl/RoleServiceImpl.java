package com.fks.service.impl;

import com.fks.dao.IRoleDao;
import com.fks.domain.Role;
import com.fks.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private IRoleDao roleDao;

    @Override
    public Role findRoleById(Integer id) {
        return roleDao.findById(id);
    }

    @Override
    public Role findRoleByIdWithRes(Integer id) {
        return roleDao.findByIdWithRes(id);
    }
}
