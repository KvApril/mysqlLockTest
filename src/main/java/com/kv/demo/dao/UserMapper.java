package com.kv.demo.dao;

import com.kv.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User queryUser(Integer id);

    User queryUserByLock(Integer id);

    List<User> showUsers();

    Integer updateAge(User user);

    Integer updateAgeByVersion(User user);

}
