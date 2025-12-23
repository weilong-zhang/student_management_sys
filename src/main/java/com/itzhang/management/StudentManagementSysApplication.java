package com.itzhang.management;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class StudentManagementSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentManagementSysApplication.class, args);
        log.info("Server Started！！！");
    }

}
