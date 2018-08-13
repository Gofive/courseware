package com.timeanime.courseware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

//@EnableDiscoveryClient
@SpringBootApplication
@ServletComponentScan
public class DeviceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceServiceApplication.class, args);
    }
}
