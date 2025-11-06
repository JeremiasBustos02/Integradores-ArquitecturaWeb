package com.microservices.gateway.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginDTO(
    @NotNull
    @Size(min = 1, max = 50)
    String username,

    @NotNull
    @Size(min = 4, max = 100)
    String password,

    Boolean rememberMe
) {}
