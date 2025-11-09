package com.microservices.facturacion.service;

import com.microservices.facturacion.dto.request.FacturaRequestDTO;
import com.microservices.facturacion.dto.response.FacturaResponseDTO;
import com.microservices.facturacion.dto.response.ReporteTotalFacturadoDTO;
import com.microservices.facturacion.entity.EstadoFactura;

import java.time.LocalDateTime;
import java.util.List;

public interface IFacturaService {
    
    FacturaResponseDTO crearFactura(FacturaRequestDTO requestDTO);
    
    FacturaResponseDTO actualizarFactura(Long id, FacturaRequestDTO requestDTO);
    
    FacturaResponseDTO obtenerFacturaPorId(Long id);
    
    List<FacturaResponseDTO> obtenerTodasLasFacturas();
    
    void eliminarFactura(Long id);
    
    List<FacturaResponseDTO> obtenerFacturasPorCuenta(Long cuentaId);
    
    List<FacturaResponseDTO> obtenerFacturasPorEstado(EstadoFactura estado);
    
    List<FacturaResponseDTO> obtenerFacturasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<FacturaResponseDTO> obtenerFacturasPorCuentaYRangoFechas(Long cuentaId, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    ReporteTotalFacturadoDTO obtenerTotalFacturadoEnRangoMeses(Integer mesInicio, Integer mesFin, Integer anio);
    
    FacturaResponseDTO cambiarEstadoFactura(Long id, EstadoFactura nuevoEstado);
}

