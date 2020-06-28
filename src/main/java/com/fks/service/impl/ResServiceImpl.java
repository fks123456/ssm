package com.fks.service.impl;

import com.fks.dao.IResDao;
import com.fks.service.IResService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResServiceImpl implements IResService {
    @Autowired
    private IResDao resDao;

    @Override
    public List<String> findAllResNameByRoleId(Integer role_id) {
        return resDao.findAllResNameByRoleId(role_id);
    }
}
