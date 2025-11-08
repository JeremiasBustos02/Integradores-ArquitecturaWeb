package com.microservices.facturacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class FacturacionMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacturacionMicroserviceApplication.class, args);
    }
}

