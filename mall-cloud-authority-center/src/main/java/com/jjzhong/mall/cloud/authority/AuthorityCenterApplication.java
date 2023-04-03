package com.jjzhong.mall.cloud.authority;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.jjzhong.mall.cloud.*")
@MapperScan(basePackages = "com.jjzhong.mall.cloud.authority.model.dao")
public class AuthorityCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorityCenterApplication.class, args);
    }
}
