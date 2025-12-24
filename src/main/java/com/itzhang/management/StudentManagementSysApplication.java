package com.itzhang.management;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.itzhang.management.mapper")
@SpringBootApplication
@Slf4j
public class StudentManagementSysApplication {

    /**
     * @param args
     * @return void
     * @Description 程序启动入口
     * @Author weiloong_zhang
     */
    public static void main(String[] args) {
        SpringApplication.run(StudentManagementSysApplication.class, args);
        log.info("Server Started！！！");
    }

}
