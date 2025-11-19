package com.microservices.usuarios.service;

import com.microservices.usuarios.dto.request.UsuarioRequestDTO;
import com.microservices.usuarios.dto.response.CuentaResponseDTO;
import com.microservices.usuarios.dto.response.UsuarioAuthResponseDTO;
import com.microservices.usuarios.dto.response.UsuarioResponseDTO;
import com.microservices.usuarios.dto.response.UsuarioUsoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface IUsuarioService {

    UsuarioResponseDTO createUsuario(UsuarioRequestDTO requestDTO);

    UsuarioAuthResponseDTO getUsuarioByEmailForAuth(String email);

    UsuarioResponseDTO getUsuarioById(Long id);

    UsuarioResponseDTO getUsuarioByEmail(String email);

    UsuarioResponseDTO getUsuarioByCelular(String celular);

    Page<UsuarioResponseDTO> getAllUsuarios(Pageable pageable);

    Page<UsuarioResponseDTO> searchUsuariosByName(String nombre, String apellido, Pageable pageable);

    Page<UsuarioResponseDTO> getUsuariosByCuentaId(Long cuentaId, Pageable pageable);

    List<CuentaResponseDTO> getCuentasByUsuarioId(Long usuarioId);

    UsuarioResponseDTO updateUsuario(Long id, UsuarioRequestDTO requestDTO);

    void deleteUsuario(Long id);

    void asociarUsuarioACuenta(Long usuarioId, Long cuentaId);

    void desasociarUsuarioDeCuenta(Long usuarioId, Long cuentaId);

    Long getCuentaParaFacturar(Long usuarioId);

    List<UsuarioUsoDTO> getUsuariosMasFrecuentes(LocalDate desde, LocalDate hasta, String tipoCuenta);
}
