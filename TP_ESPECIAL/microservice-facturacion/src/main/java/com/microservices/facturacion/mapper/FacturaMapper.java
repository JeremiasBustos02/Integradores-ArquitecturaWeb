package com.microservices.facturacion.mapper;

import com.microservices.facturacion.dto.request.FacturaRequestDTO;
import com.microservices.facturacion.dto.response.FacturaResponseDTO;
import com.microservices.facturacion.entity.Factura;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FacturaMapper {
    
    FacturaResponseDTO toResponseDTO(Factura factura);
    
    Factura toEntity(FacturaRequestDTO requestDTO);
}

