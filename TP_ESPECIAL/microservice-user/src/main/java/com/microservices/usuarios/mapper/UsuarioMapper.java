package com.microservices.usuarios.mapper;

import com.microservices.usuarios.dto.request.UsuarioRequestDTO;
import com.microservices.usuarios.dto.response.UsuarioBasicResponseDTO;
import com.microservices.usuarios.dto.response.UsuarioResponseDTO;
import com.microservices.usuarios.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CuentaMapper.class})
public interface UsuarioMapper {
    
    Usuario toEntity(UsuarioRequestDTO requestDTO);
    
    @Mapping(target = "cuentas", source = "cuentas")
    UsuarioResponseDTO toResponseDTO(Usuario usuario);
    
    UsuarioBasicResponseDTO toBasicResponseDTO(Usuario usuario);
    
    List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> usuarios);
    
    List<UsuarioBasicResponseDTO> toBasicResponseDTOList(List<Usuario> usuarios);
}
