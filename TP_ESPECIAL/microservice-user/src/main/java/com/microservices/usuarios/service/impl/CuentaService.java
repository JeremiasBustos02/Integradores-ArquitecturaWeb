package com.microservices.usuarios.service.impl;

import com.microservices.usuarios.dto.request.CuentaRequestDTO;
import com.microservices.usuarios.dto.response.CuentaResponseDTO;
import com.microservices.usuarios.entity.Cuenta;
import com.microservices.usuarios.entity.TipoCuenta;
import com.microservices.usuarios.exception.CuentaNotFoundException;
import com.microservices.usuarios.exception.DuplicateResourceException;
import com.microservices.usuarios.mapper.CuentaMapper;
import com.microservices.usuarios.repository.CuentaRepository;
import com.microservices.usuarios.service.ICuentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CuentaService implements ICuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;

    @Override
    public CuentaResponseDTO createCuenta(CuentaRequestDTO requestDTO) {
        log.info("Creando cuenta con ID Mercado Pago: {}", requestDTO.getIdCuentaMercadoPago());
        
        // Validar que no exista una cuenta con el mismo ID de Mercado Pago
        if (cuentaRepository.existsByIdCuentaMercadoPago(requestDTO.getIdCuentaMercadoPago())) {
            throw new DuplicateResourceException("Ya existe una cuenta con el ID de Mercado Pago: " + requestDTO.getIdCuentaMercadoPago());
        }

        Cuenta cuenta = cuentaMapper.toEntity(requestDTO);
        Cuenta savedCuenta = cuentaRepository.save(cuenta);
        
        log.info("Cuenta creada exitosamente con ID: {}", savedCuenta.getId());
        return cuentaMapper.toResponseDTO(savedCuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaResponseDTO getCuentaById(Long id) {
        log.info("Buscando cuenta con ID: {}", id);
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException(id));
        return cuentaMapper.toResponseDTO(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaResponseDTO getCuentaByIdMercadoPago(Long idMercadoPago) {
        log.info("Buscando cuenta con ID Mercado Pago: {}", idMercadoPago);
        Cuenta cuenta = cuentaRepository.findByIdCuentaMercadoPago(idMercadoPago)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con ID Mercado Pago: " + idMercadoPago));
        return cuentaMapper.toResponseDTO(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CuentaResponseDTO> getAllCuentas(Pageable pageable) {
        log.info("Obteniendo todas las cuentas - página: {}, tamaño: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Cuenta> cuentas = cuentaRepository.findAll(pageable);
        return cuentas.map(cuentaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CuentaResponseDTO> getCuentasByEstado(Boolean estadoCuenta, Pageable pageable) {
        log.info("Obteniendo cuentas por estado: {}", estadoCuenta);
        Page<Cuenta> cuentas = cuentaRepository.findByEstadoCuenta(estadoCuenta, pageable);
        return cuentas.map(cuentaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CuentaResponseDTO> getCuentasByTipo(TipoCuenta tipoCuenta, Pageable pageable) {
        log.info("Obteniendo cuentas por tipo: {}", tipoCuenta);
        Page<Cuenta> cuentas = cuentaRepository.findByTipoCuenta(tipoCuenta, pageable);
        return cuentas.map(cuentaMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CuentaResponseDTO> getCuentasByUsuarioId(Long usuarioId, Pageable pageable) {
        log.info("Obteniendo cuentas del usuario ID: {}", usuarioId);
        Page<Cuenta> cuentas = cuentaRepository.findByUsuarioId(usuarioId, pageable);
        return cuentas.map(cuentaMapper::toResponseDTO);
    }

    @Override
    public CuentaResponseDTO habilitarCuenta(Long id) {
        log.info("Habilitando cuenta con ID: {}", id);
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException(id));
        
        cuenta.setEstadoCuenta(true);
        Cuenta updatedCuenta = cuentaRepository.save(cuenta);
        
        log.info("Cuenta habilitada exitosamente con ID: {}", id);
        return cuentaMapper.toResponseDTO(updatedCuenta);
    }

    @Override
    public CuentaResponseDTO deshabilitarCuenta(Long id) {
        log.info("Deshabilitando cuenta con ID: {}", id);
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException(id));
        
        cuenta.setEstadoCuenta(false);
        Cuenta updatedCuenta = cuentaRepository.save(cuenta);
        
        log.info("Cuenta deshabilitada exitosamente con ID: {}", id);
        return cuentaMapper.toResponseDTO(updatedCuenta);
    }

    @Override
    public CuentaResponseDTO actualizarTipoCuenta(Long id, TipoCuenta tipoCuenta) {
        log.info("Actualizando tipo de cuenta ID: {} a tipo: {}", id, tipoCuenta);
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException(id));
        
        cuenta.setTipoCuenta(tipoCuenta);
        if (tipoCuenta == TipoCuenta.PREMIUM) {
            cuenta.renovarCupoPremium();
        }
        
        Cuenta updatedCuenta = cuentaRepository.save(cuenta);
        log.info("Tipo de cuenta actualizado exitosamente");
        return cuentaMapper.toResponseDTO(updatedCuenta);
    }

    @Override
    public CuentaResponseDTO cargarSaldo(Long id, BigDecimal monto) {
        log.info("Cargando saldo de {} a cuenta ID: {}", monto, id);
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException(id));
        
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        
        cuenta.setSaldo(cuenta.getSaldo().add(monto));
        Cuenta updatedCuenta = cuentaRepository.save(cuenta);
        
        log.info("Saldo cargado exitosamente. Nuevo saldo: {}", updatedCuenta.getSaldo());
        return cuentaMapper.toResponseDTO(updatedCuenta);
    }

    @Override
    public CuentaResponseDTO descontarSaldo(Long id, BigDecimal monto) {
        log.info("Descontando saldo de {} a cuenta ID: {}", monto, id);
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException(id));
        
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        
        if (cuenta.getSaldo().compareTo(monto) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        
        cuenta.setSaldo(cuenta.getSaldo().subtract(monto));
        Cuenta updatedCuenta = cuentaRepository.save(cuenta);
        
        log.info("Saldo descontado exitosamente. Nuevo saldo: {}", updatedCuenta.getSaldo());
        return cuentaMapper.toResponseDTO(updatedCuenta);
    }

    @Override
    public CuentaResponseDTO actualizarKilometros(Long id, Double kilometros) {
        log.info("Actualizando kilómetros de cuenta ID: {} con {} km", id, kilometros);
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException(id));
        
        cuenta.setKilometrosRecorridosMes(cuenta.getKilometrosRecorridosMes() + kilometros);
        Cuenta updatedCuenta = cuentaRepository.save(cuenta);
        
        log.info("Kilómetros actualizados exitosamente. Total: {} km", updatedCuenta.getKilometrosRecorridosMes());
        return cuentaMapper.toResponseDTO(updatedCuenta);
    }

    @Override
    public void deleteCuenta(Long id) {
        log.info("Eliminando cuenta con ID: {}", id);
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException(id));
        
        // Desasociar de todos los usuarios antes de eliminar
        cuenta.getUsuarios().forEach(usuario -> usuario.removeCuenta(cuenta));
        
        cuentaRepository.delete(cuenta);
        log.info("Cuenta eliminada exitosamente con ID: {}", id);
    }

    @Override
    public void renovarCupoPremium(Long id) {
        log.info("Renovando cupo premium para cuenta ID: {}", id);
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException(id));
        
        if (cuenta.getTipoCuenta() != TipoCuenta.PREMIUM) {
            throw new IllegalArgumentException("Solo las cuentas premium pueden renovar su cupo");
        }
        
        cuenta.renovarCupoPremium();
        cuentaRepository.save(cuenta);
        
        log.info("Cupo premium renovado exitosamente para cuenta ID: {}", id);
    }
}
