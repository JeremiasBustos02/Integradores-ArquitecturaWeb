package com.microservices.parada;

import jakarta.persistence.Entity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroserviceParadaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceParadaApplication.class, args);
    }

}
