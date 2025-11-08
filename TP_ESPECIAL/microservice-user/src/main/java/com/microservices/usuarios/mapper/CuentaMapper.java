package com.microservices.usuarios.mapper;

import com.microservices.usuarios.dto.request.CuentaRequestDTO;
import com.microservices.usuarios.dto.response.CuentaBasicResponseDTO;
import com.microservices.usuarios.dto.response.CuentaResponseDTO;
import com.microservices.usuarios.entity.Cuenta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UsuarioMapper.class})
public interface CuentaMapper {
    
    @Mapping(target = "saldo", source = "saldoInicial")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaAlta", ignore = true)
    @Mapping(target = "estadoCuenta", constant = "true")
    @Mapping(target = "kilometrosRecorridosMes", constant = "0.0")
    @Mapping(target = "fechaRenovacionCupo", ignore = true)
    @Mapping(target = "usuarios", ignore = true)
    Cuenta toEntity(CuentaRequestDTO requestDTO);
    
    @Mapping(target = "usuarios", source = "usuarios")
    CuentaResponseDTO toResponseDTO(Cuenta cuenta);
    
    CuentaBasicResponseDTO toBasicResponseDTO(Cuenta cuenta);
    
    List<CuentaResponseDTO> toResponseDTOList(List<Cuenta> cuentas);
    
    List<CuentaBasicResponseDTO> toBasicResponseDTOList(List<Cuenta> cuentas);
}
