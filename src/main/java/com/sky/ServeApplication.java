package com.sky;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 主启动类
 */
@SpringBootApplication
@MapperScan("com.sky.mapper")
public class ServeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServeApplication.class, args);
        System.out.println("================================");
        System.out.println("启动成功(*^▽^*)");
        System.out.println("================================");
    }

}
