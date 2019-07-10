package com.knowledge.practice.service;

import com.whalin.MemCached.MemCachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * memcache操作工具类
 */
@Component
public class MemCacheUtil {

    private Logger logger = LoggerFactory.getLogger(MemCacheUtil.class);

    /**
     * 分布式锁的key
     */
    private static final String DISTRIBUTED_LOCK_KEY = "distributedLockKey";

    /**
     * 分布式锁的默认value
     */
    private static final String DEFAULT_VALUE = "123";

    /**
     * 分布式锁的默认时间1秒
     */
    private static final int DEFAULT_LOCK_TIME = 1;

    @Autowired
    private MemCachedClient memCachedClient;
    
    /**
     * 获取过期时间
     * @param time 秒
     * @return
     */
    private Date getExpireDate(int time) {
        long expireTime = System.currentTimeMillis() + time * 1000;
        return new Date(expireTime);
    }

    /**
     * 覆盖此key值
     *
     * @param key
     * @param object
     * @param time
     * @return
     */
    public boolean put(String key, Object object, int time) {
        return memCachedClient.set(key, object, getExpireDate(time));
    }

    /**
     * 如果key不存在，存进去 true
     * 如果存在，false
     *
     * @param key
     * @param object
     * @param time
     * @return
     */
    public boolean putNotExist(String key, Object object, int time) {
        return memCachedClient.add(key, object, getExpireDate(time));
    }

    /**
     * 根据key获得
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return memCachedClient.get(key);
    }

    /**
     * 根据key删除
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        return memCachedClient.delete(key);
    }

    /**
     * 不设置有效时间
     *
     * @param key
     * @param object
     * @return
     */
    public boolean put(String key, Object object) {
        return memCachedClient.set(key, object);
    }


}
