package com.analyze.stack;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.analyze.stack.mapper")
@EnableTransactionManagement
@EnableScheduling
public class StackApplication {

    public static void main(String[] args) {
        SpringApplication.run(StackApplication.class, args);
    }

}
