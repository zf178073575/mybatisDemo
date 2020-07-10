package com.zhuangfei.controller;

import com.alibaba.fastjson.JSON;
import com.zhuangfei.component.Sender;
import com.zhuangfei.entity.RoleUser;
import com.zhuangfei.serivce.UserService;
import com.zhuangfei.utils.DistributedLockHandler;
import com.zhuangfei.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Sender sender;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private DistributedLockHandler distributedLockHandler;

    @RequestMapping("/get/{name}")
    public List<RoleUser> findAll(@PathVariable(name="name") String name){
        List<RoleUser> list = userService.findAll(name);
//        String key = "dafei";
//        if(distributedLockHandler.lock(key,20, TimeUnit.SECONDS)) {
//            try {
//                if (null == redisUtil.get(key)) {
//                    list = userService.findAll(name);
//                    String str = JSON.toJSONString(list);
//                    redisUtil.set(key, str, 20);
//                    System.out.println("db");
//                } else {
//                    String value = redisUtil.get(key).toString();
//                    list = JSON.parseArray(value, RoleUser.class);
//                    System.out.println("redis");
//                }
//            } catch (Exception e) {
//                throw e;
//            } finally {
//                distributedLockHandler.unlock(key);
//            }
//        }
        String json = JSON.toJSONString(list);
        sender.sendExchange(json);
        return list;
    }

    @Value("${nacos.test.propertie:123}")
    private String testProperties;

    @GetMapping("/test")
    public String test(){
        return testProperties;
    }

}
