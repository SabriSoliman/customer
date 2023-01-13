package com.digitaltrack.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableFeignClients(basePackages = "com.digitaltrack.clients")
@ComponentScan( basePackages= {
        "com.digitaltrack.customer",
        "com.digitaltrack.amqp"
})
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}