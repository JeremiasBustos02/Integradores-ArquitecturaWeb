package com.microservices.facturacion.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteTotalFacturadoDTO {
    
    private Integer mesInicio;
    private Integer mesFin;
    private Integer anio;
    private Double totalFacturado;
    private Long cantidadFacturas;
}

