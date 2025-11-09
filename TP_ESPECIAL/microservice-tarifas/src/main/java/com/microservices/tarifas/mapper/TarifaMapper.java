package com.microservices.tarifas.mapper;

import com.microservices.tarifas.dto.request.TarifaRequestDTO;
import com.microservices.tarifas.dto.response.TarifaResponseDTO;
import com.microservices.tarifas.entity.Tarifa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TarifaMapper {
    
    TarifaResponseDTO toResponseDTO(Tarifa tarifa);
    
    Tarifa toEntity(TarifaRequestDTO requestDTO);
}

