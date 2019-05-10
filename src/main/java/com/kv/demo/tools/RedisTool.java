package com.kv.demo.tools;

import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.Random;

public class RedisTool {
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = ("EX");
    private static final Long   RELEASE_SUCCESS = 1L;
    private static Random r = new Random();


    /**
     *
     * @param lockKey    锁
     * @param reqId      请求标识
     * @param expireTime 超期时间
     * @return  是否获取成功
     */
    public static boolean tryGetDistributedLock(String lockKey, String reqId, int expireTime) {
        System.out.println(lockKey);
        Jedis jedis = new Jedis("localhost",6379);
        jedis.set("hola","hola");
        String result = jedis.set(lockKey, reqId, SET_IF_NOT_EXIST,SET_WITH_EXPIRE_TIME,expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    public static boolean releaseDistributedLock(String lockKey, String reqId) {
        System.out.println(lockKey);
        Jedis jedis = new Jedis("localhost",6379);
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(reqId));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }
}
