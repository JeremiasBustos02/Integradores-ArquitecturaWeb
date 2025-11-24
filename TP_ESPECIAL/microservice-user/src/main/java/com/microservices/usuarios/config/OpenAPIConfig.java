package com.microservices.usuarios.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.domain.Pageable;
import java.util.Arrays;

@Configuration
public class OpenAPIConfig {
    static {
        SpringDocUtils.getConfig().addRequestWrapperToIgnore(Pageable.class);
    }

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "bearerAuth";

        Server gatewayServer = new Server();
        gatewayServer.setUrl("http://localhost:8080");
        gatewayServer.setDescription("Gateway (Producción)");

        Server directServer = new Server();
        directServer.setUrl("http://localhost:8083");
        directServer.setDescription("Directo (Desarrollo)");

        return new OpenAPI()
                .info(new Info()
                        .title("Servicio de Usuarios API")
                        .version("1.0.0")
                        .description("Gestión de usuarios, roles y vinculación con cuentas"))
                .servers(Arrays.asList(gatewayServer, directServer))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingrese el token JWT (sin 'Bearer ' al inicio)")));
    }
}