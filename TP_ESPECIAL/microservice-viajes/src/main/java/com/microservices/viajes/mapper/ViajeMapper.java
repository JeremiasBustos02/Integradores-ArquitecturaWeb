package com.microservices.viajes.mapper;

import com.microservices.viajes.dto.request.ViajeRequestDTO;
import com.microservices.viajes.dto.response.BasicViajeResponseDTO;
import com.microservices.viajes.dto.response.ViajeResponseDTO;
import com.microservices.viajes.entity.Viaje;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {PausaMapper.class})
public interface ViajeMapper {
    @Mappings({
            @Mapping(source = "paradaInicioId", target = "paradaOrigenId"),
            @Mapping(source = "monopatinId", target = "monopatinId"),
            @Mapping(source = "usuarioId", target = "usuarioId"),
            @Mapping(source = "tarifaId", target = "tarifaId")
    })
    Viaje toEntity(ViajeRequestDTO requestDTO);

       // Entity -> Response
    @Mapping(source = "distanciaRecorrida", target = "kmRecorridos")
    ViajeResponseDTO toResponseDTO(Viaje viaje);

    BasicViajeResponseDTO toBasicReponseDTO(Viaje viaje);

    List<ViajeResponseDTO> toResponseDTOList(List<Viaje> viajes);

    List<BasicViajeResponseDTO> toBasicResponseDTOList(List<Viaje> viajes);
}
