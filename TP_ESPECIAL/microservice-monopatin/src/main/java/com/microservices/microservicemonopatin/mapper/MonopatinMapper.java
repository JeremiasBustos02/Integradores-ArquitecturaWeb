package com.microservices.microservicemonopatin.mapper;

import com.microservices.microservicemonopatin.dto.MonopatinDTO;
import com.microservices.microservicemonopatin.dto.MonopatinCercanoDTO;
import com.microservices.microservicemonopatin.dto.MonopatinRequestDTO;
import com.microservices.microservicemonopatin.entity.Monopatin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MonopatinMapper {

    // Request → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kmTotales", constant = "0.0")
    @Mapping(target = "tiempoUsoTotal", constant = "0L")
    @Mapping(target = "tiempoPausasTotales", constant = "0L")
    @Mapping(target = "enMantenimiento", constant = "false")
    Monopatin toEntity(MonopatinRequestDTO dto);

    // Entity → DTO general
    MonopatinDTO toDTO(Monopatin monopatin);

    // Entity → DTO para lista
    List<MonopatinDTO> toDTOList(List<Monopatin> monopatines);

    // Entity → DTO de monopatín cercano
    @Mapping(target = "distancia", ignore = true) // la calcula el servicio
    MonopatinCercanoDTO toCercanoDTO(Monopatin monopatin);
}
