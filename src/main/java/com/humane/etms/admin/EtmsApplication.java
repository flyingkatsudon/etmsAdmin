package com.humane.etms.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EtmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EtmsApplication.class, args);
    }
}