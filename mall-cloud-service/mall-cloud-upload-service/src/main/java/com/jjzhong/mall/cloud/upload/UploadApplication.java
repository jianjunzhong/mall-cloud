package com.jjzhong.mall.cloud.upload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "com.jjzhong.mall.cloud.*")
@EnableDiscoveryClient
public class UploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(UploadApplication.class, args);
    }
}
