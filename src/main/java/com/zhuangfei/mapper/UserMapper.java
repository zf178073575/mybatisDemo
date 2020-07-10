package com.zhuangfei.mapper;

import com.zhuangfei.entity.RoleUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<RoleUser> findAll(String name);
}
