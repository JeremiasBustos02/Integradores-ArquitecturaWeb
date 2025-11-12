package com.microservices.viajes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroserviceViajesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceViajesApplication.class, args);
    }

}
