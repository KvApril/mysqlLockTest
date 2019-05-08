package com.kv.demo.controller;

import com.kv.demo.dao.UserMapper;
import com.kv.demo.entity.User;
import com.kv.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/users")
    public List<User> showUsers() {
        return userService.showUsers();
    }

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

}
