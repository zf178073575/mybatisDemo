package com.zhuangfei.serivce;

import com.zhuangfei.entity.RoleUser;

import java.util.List;

public interface UserService {
    List<RoleUser> findAll(String name);
}
