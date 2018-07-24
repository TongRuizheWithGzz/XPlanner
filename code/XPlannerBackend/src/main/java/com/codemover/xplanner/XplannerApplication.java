package com.codemover.xplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class XplannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(XplannerApplication.class, args);
    }
}


