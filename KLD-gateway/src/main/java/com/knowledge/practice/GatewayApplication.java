package com.knowledge.practice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 网关入口启动类
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@MapperScan("com.knowledge.practice.dao.mapper")
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    /*@Bean
    public SecurityFilter securityFilter() {
        return new SecurityFilter();
    }

    @Bean
    public ResponseFilter responseFilter() {
        return new ResponseFilter();
    }

    @Bean
    public MemcacheSessionFilter memcacheSessionFilter() {
        return new MemcacheSessionFilter();
    }*/
}
