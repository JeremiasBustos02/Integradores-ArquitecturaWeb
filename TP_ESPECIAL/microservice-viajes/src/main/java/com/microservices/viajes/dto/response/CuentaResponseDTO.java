package com.microservices.viajes.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
@Getter

public class CuentaResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String celular;
    private String email;
    private LocalDateTime fechaAlta;
    private Set<CuentaBasicResponseDTO> cuentas;
}
