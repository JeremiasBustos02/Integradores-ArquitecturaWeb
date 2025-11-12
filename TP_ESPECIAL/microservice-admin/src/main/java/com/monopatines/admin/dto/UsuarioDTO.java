package com.monopatines.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String celular;
    private List<CuentaBasicDTO> cuentas;
}

