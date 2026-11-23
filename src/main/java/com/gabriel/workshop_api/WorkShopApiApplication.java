package com.gabriel.workshop_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching
public class WorkShopApiApplication {

    public static void main(String[] args) {

        SpringApplication.run(WorkShopApiApplication.class, args);
    }
}
