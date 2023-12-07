package com.example.withoutjein.Shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.withoutjein")
public class WithoutjeinApplication {

    public static void main(String[] args) {
        SpringApplication.run(WithoutjeinApplication.class, args);
    }

}
