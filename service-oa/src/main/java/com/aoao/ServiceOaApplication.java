package com.aoao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@MapperScan(value = "com.aoao.mapper")
@CrossOrigin
public class ServiceOaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceOaApplication.class, args);
    }

}
