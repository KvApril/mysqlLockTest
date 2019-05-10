package com.kv.demo.service;

import com.kv.demo.annotation.IsTryAgain;
import com.kv.demo.dao.ContentMapper;
import com.kv.demo.dao.UserMapper;
import com.kv.demo.entity.Content;
import com.kv.demo.entity.User;
import com.kv.demo.exception.ApiResultEnum;
import com.kv.demo.exception.TryAgainException;
import com.kv.demo.tools.RedisTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ContentMapper contentMapper;

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

    @IsTryAgain
    @Transactional(rollbackFor = {Exception.class,TryAgainException.class})
    public Integer updateByRedisLock(Integer id) {
        String redisKey = "updateByRedisLock.userId."+id;
        String uuid = UUID.randomUUID().toString();
        Content content = new Content();
        content.setTitle("title_"+uuid);
        content.setTdesc("desc_"+uuid);
        contentMapper.insert(content);
        User user = userMapper.queryUser(id);
        user.setAge(user.getAge()+1);
        boolean lock = RedisTool.tryGetDistributedLock(redisKey,uuid,20);
        if (lock) {
            Integer result = userMapper.updateAge(user);
            RedisTool.releaseDistributedLock(redisKey,uuid);
            return result;
        }else {
            throw new TryAgainException(ApiResultEnum.ERROR_TRY_AGAIN);
        }
    }

    @Transactional
    public Integer updateUserByRedisLockAop(Integer id) {
        System.out.println("updateUserByRedisLockAop: ");
        String uuid = UUID.randomUUID().toString();
        Content content = new Content();
        content.setTitle("title_"+uuid);
        content.setTdesc("desc_"+uuid);
        contentMapper.insert(content);

        User user = userMapper.queryUser(id);
        System.out.println("before age is:"+user.getAge());
        Integer age = user.getAge() + 1;
        user.setAge(age);
        System.out.println("age+1 is: "+user.getAge());
        Integer result = userMapper.updateAge(user);
        return result;
    }

    public Integer updateUserNormal(Integer id) {
        User user = userMapper.queryUser(id);
        System.out.println("before age is:"+user.getAge());
        Integer age = user.getAge() + 1;
        user.setAge(age);
        System.out.println("age+1 is: "+user.getAge());
        Integer result = userMapper.updateAge(user);
        return result;
    }
}
