package com.microservices.usuarios.service;

import com.microservices.usuarios.dto.request.CuentaRequestDTO;
import com.microservices.usuarios.dto.response.CuentaResponseDTO;
import com.microservices.usuarios.entity.TipoCuenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ICuentaService {
    
    CuentaResponseDTO createCuenta(CuentaRequestDTO requestDTO);
    
    CuentaResponseDTO getCuentaById(Long id);
    
    CuentaResponseDTO getCuentaByIdMercadoPago(Long idMercadoPago);
    
    Page<CuentaResponseDTO> getAllCuentas(Pageable pageable);
    
    Page<CuentaResponseDTO> getCuentasByEstado(Boolean estadoCuenta, Pageable pageable);
    
    Page<CuentaResponseDTO> getCuentasByTipo(TipoCuenta tipoCuenta, Pageable pageable);
    
    Page<CuentaResponseDTO> getCuentasByUsuarioId(Long usuarioId, Pageable pageable);
    
    CuentaResponseDTO habilitarCuenta(Long id);
    
    CuentaResponseDTO deshabilitarCuenta(Long id);
    
    CuentaResponseDTO actualizarTipoCuenta(Long id, TipoCuenta tipoCuenta);
    
    CuentaResponseDTO cargarSaldo(Long id, BigDecimal monto);
    
    CuentaResponseDTO descontarSaldo(Long id, BigDecimal monto);
    
    CuentaResponseDTO actualizarKilometros(Long id, Double kilometros);
    
    void deleteCuenta(Long id);
    
    void renovarCupoPremium(Long id);
}
