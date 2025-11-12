package com.microservices.parada.mapper;

import com.microservices.parada.dto.request.RequestParadaDTO;
import com.microservices.parada.dto.response.ResponseParadaDTO;
import com.microservices.parada.entity.Parada;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public interface ParadaMapper {
    @Mapping(target = "id", ignore = true)
    Parada toEntity(RequestParadaDTO requestParadaDTO);

    List<ResponseParadaDTO> toResponseDTOList(List<Parada> paradas);

    ResponseParadaDTO toResponseDTO(Parada parada);

}
