package com.fks.service;

import com.fks.domain.Role;

public interface IRoleService {
    public Role findRoleById(Integer id);

    public Role findRoleByIdWithRes(Integer id);
}
