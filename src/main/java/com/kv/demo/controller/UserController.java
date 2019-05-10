package com.kv.demo.controller;

import com.kv.demo.annotation.RedisLocked;
import com.kv.demo.dao.UserMapper;
import com.kv.demo.entity.User;
import com.kv.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/users")
    public List<User> showUsers() {
        return userService.showUsers();
    }

    /**
     * 使用mysql悲观锁 for update
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/update/user/{id}")
    public Integer updateUserByBlock(@PathVariable("id") Integer id ) throws Exception{
        System.out.println("更新开始----------");
        return userService.updateUserByLock(id);
    }

    @RequestMapping("/update/userbyversion/{id}")
    public Integer updateUserByVersion(@PathVariable("id") Integer id ) throws Exception{
        System.out.println("更新开始----------");
        return userService.updateUserByVersion(id);
    }

    /**
     * 使用mysql乐观锁
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/update/userbyredislock/{id}")
    public Integer updateUserByRedisLock(@PathVariable("id") Integer id ) throws Exception{
        System.out.println("更新开始----------");
        return userService.updateByRedisLock(id);
    }


    /**
     * 使用redis锁
     * @param id
     * @return
     */
    @RedisLocked(key = "'lock:user:update:'+#id", value = "操作太频繁，请等待服务器处理")
    @RequestMapping(value = "/update/useAop/{id}")
    public Integer updateUserByRedisLockAop(@PathVariable("id") Integer id ) {
        System.out.println("更新开始-------params:"+id);
        return userService.updateUserByRedisLockAop(id);
    }

    @RequestMapping(value = "/update/normal/{id}")
    public Integer updateUserNormal(@PathVariable("id") Integer id ) {
        System.out.println("更新开始-------params:"+id);
        return userService.updateUserNormal(id);
    }

}
