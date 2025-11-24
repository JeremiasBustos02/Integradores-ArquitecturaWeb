package com.microservices.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class GatewayRoutesConfig {

    @RestController
    public static class OpenApiProxyController {

        @Autowired
        private DiscoveryClient discoveryClient;

        private final RestTemplate restTemplate = new RestTemplate();

        @GetMapping("/v3/api-docs/usuarios")
        public String getUsuariosApiDocs() {
            return proxyRequest("MICROSERVICE-USER");
        }

        @GetMapping("/v3/api-docs/viajes")
        public String getViajesApiDocs() {
            return proxyRequest("MICROSERVICE-VIAJES");
        }

        @GetMapping("/v3/api-docs/monopatin")
        public String getMonopatinesApiDocs() {
            return proxyRequest("MICROSERVICE-MONOPATIN");
        }

        @GetMapping("/v3/api-docs/tarifas")
        public String getTarifasApiDocs() {
            return proxyRequest("MICROSERVICE-TARIFAS");
        }

        @GetMapping("/v3/api-docs/paradas")
        public String getParadasApiDocs() {
            return proxyRequest("MICROSERVICE-PARADA");
        }

        @GetMapping("/v3/api-docs/chat")
        public String getChatApiDocs() {
            return proxyRequest("MICROSERVICE-CHAT");
        }

        @GetMapping("/v3/api-docs/facturacion")
        public String getFacturacionApiDocs() {
            return proxyRequest("MICROSERVICE-FACTURACION");
        }

        private String proxyRequest(String serviceName) {
            try {
                List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
                if (instances.isEmpty()) {
                    return "{\"error\": \"Service " + serviceName + " not found in Eureka\"}";
                }

                ServiceInstance instance = instances.get(0);
                String url = instance.getUri().toString() + "/v3/api-docs";

                return restTemplate.getForObject(url, String.class);
            } catch (Exception e) {
                return "{\"error\": \"" + e.getMessage() + "\"}";
            }
        }
    }
}