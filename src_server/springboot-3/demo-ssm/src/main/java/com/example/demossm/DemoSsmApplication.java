package com.example.demossm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@MapperScan("com.example.demossm.mapper")
@SpringBootApplication
public class DemoSsmApplication {

    public static void main(String[] args) {
        //方法一
        SpringApplication.run(DemoSsmApplication.class, args);
    }
}
