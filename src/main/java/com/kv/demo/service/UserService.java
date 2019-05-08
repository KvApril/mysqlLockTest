package com.kv.demo.service;

import com.kv.demo.annotation.IsTryAgain;
import com.kv.demo.dao.UserMapper;
import com.kv.demo.entity.User;
import com.kv.demo.exception.ApiResultEnum;
import com.kv.demo.exception.TryAgainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<User> showUsers() {
        return userMapper.showUsers();
    }

    @Transactional
    public Integer updateUserByLock(Integer id) throws Exception{
        User user = userMapper.queryUserByLock(id);
        System.out.println("当前age: "+user.getAge());
        user.setAge(user.getAge()+1);
        userMapper.updateAge(user);
        user = userMapper.queryUser(id);
        System.out.println("更新之后:"+user.getAge());
        return user.getAge();
    }

    @IsTryAgain
    @Transactional
    public Integer updateUserByVersion(Integer id) throws Exception{
        User user = userMapper.queryUser(id);
        System.out.println("当前age: "+user.getAge());
        user.setAge(user.getAge()+1);
        Integer result = userMapper.updateAgeByVersion(user);
        if(result < 1){
            //如果更新失败就抛出去，重试
            throw new TryAgainException(ApiResultEnum.ERROR_TRY_AGAIN);
        }
        System.out.println("result: " + result);
        user = userMapper.queryUser(id);
        System.out.println("更新之后:"+user.getAge());
        return user.getAge();
    }

}
