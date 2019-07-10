package com.knowledge.practice.config;

import com.whalin.MemCached.MemCachedClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.whalin.MemCached.SockIOPool;

/**
 * 配置memcache连接池
 */
@Configuration
public class MemcacheConfig {

    @Value("${memcache.servers:}")
    private String[] servers;

    @Value("${memcache.weights:5}")
    private Integer[] weights;

    @Value("${memcache.initConn:20}")
    private int initConn;

    @Value("${memcache.minConn:10}")
    private int minConn;

    @Value("${memcache.maxConn:50}")
    private int maxConn;

    @Value("${memcache.maintSleep:3000}")
    private long maintSleep;

    @Value("${memcache.nagle:false}")
    private boolean nagle;

    @Value("${memcache.socketTO:3000}")
    private int socketTO;

    @Bean
    public SockIOPool sockIOPool() {
        // 获取连接池的实例
        SockIOPool pool = SockIOPool.getInstance();
        // 服务器列表及其权重
        // 设置服务器信息
        pool.setServers(servers);
        pool.setWeights(weights);
        // 设置初始连接数、最小连接数、最大连接数、最大处理时间
        pool.setInitConn(initConn);
        pool.setMinConn(minConn);
        pool.setMaxConn(maxConn);
        // 设置连接池守护线程的睡眠时间
        pool.setMaintSleep(maintSleep);
        // 设置TCP参数，连接超时
        pool.setNagle(nagle);
        pool.setSocketConnectTO(socketTO);
        // 初始化并启动连接池
        pool.initialize();
        return pool;
    }

    @Bean
    @ConditionalOnBean(SockIOPool.class)
    public MemCachedClient memCachedClient() {
        return new MemCachedClient();
    }

}
