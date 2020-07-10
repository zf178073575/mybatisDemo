package com.zhuangfei.service.impl;

import com.zhuangfei.entity.RoleUser;
import com.zhuangfei.mapper.UserMapper;
import com.zhuangfei.serivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSerivceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<RoleUser> findAll(String name) {
        return userMapper.findAll(name);
    }
}
