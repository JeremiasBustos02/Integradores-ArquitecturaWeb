package com.microservices.microserviceiaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RespuestaApi<T> {
private boolean ok;
private String mensaje;
private T Datos;
}
