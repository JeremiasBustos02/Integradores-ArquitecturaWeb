package com.microservices.facturacion.service.impl;

import com.microservices.facturacion.client.TarifaFeignClient;
import com.microservices.facturacion.dto.request.FacturaRequestDTO;
import com.microservices.facturacion.dto.response.FacturaResponseDTO;
import com.microservices.facturacion.dto.response.ReporteTotalFacturadoDTO;
import com.microservices.facturacion.entity.EstadoFactura;
import com.microservices.facturacion.entity.Factura;
import com.microservices.facturacion.exception.FacturaNotFoundException;
import com.microservices.facturacion.mapper.FacturaMapper;
import com.microservices.facturacion.repository.FacturaRepository;
import com.microservices.facturacion.service.IFacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaService implements IFacturaService {
    
    private final FacturaRepository facturaRepository;
    private final FacturaMapper facturaMapper;
    private final TarifaFeignClient tarifaFeignClient;
    
    @Override
    @Transactional
    public FacturaResponseDTO crearFactura(FacturaRequestDTO requestDTO) {
        Factura factura = facturaMapper.toEntity(requestDTO);
        
        // Generar número de factura único
        factura.setNumeroFactura(generarNumeroFactura());
        
        Factura facturaGuardada = facturaRepository.save(factura);
        return facturaMapper.toResponseDTO(facturaGuardada);
    }
    
    @Override
    @Transactional
    public FacturaResponseDTO actualizarFactura(Long id, FacturaRequestDTO requestDTO) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new FacturaNotFoundException("Factura no encontrada con id: " + id));
        
        factura.setCuentaId(requestDTO.getCuentaId());
        factura.setViajeId(requestDTO.getViajeId());
        factura.setMontoTotal(requestDTO.getMontoTotal());
        factura.setFechaEmision(requestDTO.getFechaEmision());
        factura.setFechaVencimiento(requestDTO.getFechaVencimiento());
        factura.setEstado(requestDTO.getEstado());
        factura.setDescripcion(requestDTO.getDescripcion());
        factura.setPeriodoMes(requestDTO.getPeriodoMes());
        factura.setPeriodoAnio(requestDTO.getPeriodoAnio());
        factura.setTipoCuenta(requestDTO.getTipoCuenta());
        
        Factura facturaActualizada = facturaRepository.save(factura);
        return facturaMapper.toResponseDTO(facturaActualizada);
    }
    
    @Override
    @Transactional(readOnly = true)
    public FacturaResponseDTO obtenerFacturaPorId(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new FacturaNotFoundException("Factura no encontrada con id: " + id));
        return facturaMapper.toResponseDTO(factura);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> obtenerTodasLasFacturas() {
        return facturaRepository.findAll()
                .stream()
                .map(facturaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void eliminarFactura(Long id) {
        if (!facturaRepository.existsById(id)) {
            throw new FacturaNotFoundException("Factura no encontrada con id: " + id);
        }
        facturaRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> obtenerFacturasPorCuenta(Long cuentaId) {
        return facturaRepository.findByCuentaId(cuentaId)
                .stream()
                .map(facturaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> obtenerFacturasPorEstado(EstadoFactura estado) {
        return facturaRepository.findByEstado(estado)
                .stream()
                .map(facturaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> obtenerFacturasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return facturaRepository.findByFechaEmisionBetween(fechaInicio, fechaFin)
                .stream()
                .map(facturaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<FacturaResponseDTO> obtenerFacturasPorCuentaYRangoFechas(Long cuentaId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return facturaRepository.findByCuentaIdAndFechaEmisionBetween(cuentaId, fechaInicio, fechaFin)
                .stream()
                .map(facturaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ReporteTotalFacturadoDTO obtenerTotalFacturadoEnRangoMeses(Integer mesInicio, Integer mesFin, Integer anio) {
        Double totalFacturado = facturaRepository.calcularTotalFacturadoEnRangoMeses(mesInicio, mesFin, anio);
        Long cantidadFacturas = facturaRepository.contarFacturasEnRangoMeses(mesInicio, mesFin, anio);
        
        if (totalFacturado == null) {
            totalFacturado = 0.0;
        }
        if (cantidadFacturas == null) {
            cantidadFacturas = 0L;
        }
        
        return new ReporteTotalFacturadoDTO(mesInicio, mesFin, anio, totalFacturado, cantidadFacturas);
    }
    
    @Override
    @Transactional
    public FacturaResponseDTO cambiarEstadoFactura(Long id, EstadoFactura nuevoEstado) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new FacturaNotFoundException("Factura no encontrada con id: " + id));
        
        factura.setEstado(nuevoEstado);
        Factura facturaActualizada = facturaRepository.save(factura);
        return facturaMapper.toResponseDTO(facturaActualizada);
    }
    
    private String generarNumeroFactura() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        long count = facturaRepository.count() + 1;
        return "FACT-" + timestamp + "-" + String.format("%05d", count);
    }
}

