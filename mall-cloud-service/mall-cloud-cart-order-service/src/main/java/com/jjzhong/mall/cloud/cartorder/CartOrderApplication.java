package com.jjzhong.mall.cloud.cartorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan(value = "com.jjzhong.mall.cloud.cartorder.model.dao")
@SpringBootApplication(scanBasePackages = "com.jjzhong.mall.cloud.*")
@EnableDiscoveryClient
@EnableFeignClients
public class CartOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartOrderApplication.class, args);
    }
}
