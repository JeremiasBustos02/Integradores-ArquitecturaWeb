package com.microservices.usuarios.service;

import com.microservices.usuarios.dto.request.UsuarioRequestDTO;
import com.microservices.usuarios.dto.response.UsuarioResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUsuarioService {
    
    UsuarioResponseDTO createUsuario(UsuarioRequestDTO requestDTO);
    
    UsuarioResponseDTO getUsuarioById(Long id);
    
    UsuarioResponseDTO getUsuarioByEmail(String email);
    
    UsuarioResponseDTO getUsuarioByCelular(String celular);
    
    Page<UsuarioResponseDTO> getAllUsuarios(Pageable pageable);
    
    Page<UsuarioResponseDTO> searchUsuariosByName(String nombre, String apellido, Pageable pageable);
    
    Page<UsuarioResponseDTO> getUsuariosByCuentaId(Long cuentaId, Pageable pageable);
    
    UsuarioResponseDTO updateUsuario(Long id, UsuarioRequestDTO requestDTO);
    
    void deleteUsuario(Long id);
    
    void asociarUsuarioACuenta(Long usuarioId, Long cuentaId);
    
    void desasociarUsuarioDeCuenta(Long usuarioId, Long cuentaId);
}
