package com.microservices.viajes.mapper;

import com.microservices.viajes.dto.response.PausaDTO;
import com.microservices.viajes.entity.Pausa;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PausaMapper {


    PausaDTO toDTO(Pausa pausa);

    Pausa toEntity(PausaDTO pausaDTO);
}