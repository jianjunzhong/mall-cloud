package com.jjzhong.mall.cloud.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.jjzhong.mall.cloud.*")
@EnableDiscoveryClient
public class EmailApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailApplication.class, args);
    }
}
