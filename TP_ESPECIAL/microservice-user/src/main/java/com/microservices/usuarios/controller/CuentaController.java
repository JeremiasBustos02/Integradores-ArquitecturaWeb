package com.microservices.usuarios.controller;

import com.microservices.usuarios.dto.request.CuentaRequestDTO;
import com.microservices.usuarios.dto.response.CuentaResponseDTO;
import com.microservices.usuarios.entity.TipoCuenta;
import com.microservices.usuarios.service.ICuentaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final ICuentaService cuentaService;

    @PostMapping
    public ResponseEntity<CuentaResponseDTO> createCuenta(@RequestBody @Valid CuentaRequestDTO requestDTO) {
        log.info("REST request to create Cuenta with Mercado Pago ID: {}", requestDTO.getIdCuentaMercadoPago());
        CuentaResponseDTO createdCuenta = cuentaService.createCuenta(requestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/cuentas/{id}")
                .buildAndExpand(createdCuenta.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdCuenta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> getCuentaById(@PathVariable @Positive @NotNull Long id) {
        log.info("REST request to get Cuenta by ID: {}", id);
        CuentaResponseDTO cuenta = cuentaService.getCuentaById(id);
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/mercado-pago/{idMercadoPago}")
    public ResponseEntity<CuentaResponseDTO> getCuentaByIdMercadoPago(@PathVariable @Positive @NotNull Long idMercadoPago) {
        log.info("REST request to get Cuenta by Mercado Pago ID: {}", idMercadoPago);
        CuentaResponseDTO cuenta = cuentaService.getCuentaByIdMercadoPago(idMercadoPago);
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping
    public ResponseEntity<Page<CuentaResponseDTO>> getAllCuentas(Pageable pageable) {
        log.info("REST request to get all Cuentas");
        Page<CuentaResponseDTO> cuentas = cuentaService.getAllCuentas(pageable);
        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<CuentaResponseDTO>> getCuentasByEstado(
            @PathVariable Boolean estado,
            Pageable pageable) {
        log.info("REST request to get Cuentas by estado: {}", estado);
        Page<CuentaResponseDTO> cuentas = cuentaService.getCuentasByEstado(estado, pageable);
        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Page<CuentaResponseDTO>> getCuentasByTipo(
            @PathVariable TipoCuenta tipo,
            Pageable pageable) {
        log.info("REST request to get Cuentas by tipo: {}", tipo);
        Page<CuentaResponseDTO> cuentas = cuentaService.getCuentasByTipo(tipo, pageable);
        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<CuentaResponseDTO>> getCuentasByUsuarioId(
            @PathVariable @Positive @NotNull Long usuarioId,
            Pageable pageable) {
        log.info("REST request to get Cuentas by usuario ID: {}", usuarioId);
        Page<CuentaResponseDTO> cuentas = cuentaService.getCuentasByUsuarioId(usuarioId, pageable);
        return ResponseEntity.ok(cuentas);
    }

    @PatchMapping("/{id}/habilitar")
    public ResponseEntity<CuentaResponseDTO> habilitarCuenta(@PathVariable @Positive @NotNull Long id) {
        log.info("REST request to enable Cuenta ID: {}", id);
        CuentaResponseDTO cuentaHabilitada = cuentaService.habilitarCuenta(id);
        return ResponseEntity.ok(cuentaHabilitada);
    }

    @PatchMapping("/{id}/deshabilitar")
    public ResponseEntity<CuentaResponseDTO> deshabilitarCuenta(@PathVariable @Positive @NotNull Long id) {
        log.info("REST request to disable Cuenta ID: {}", id);
        CuentaResponseDTO cuentaDeshabilitada = cuentaService.deshabilitarCuenta(id);
        return ResponseEntity.ok(cuentaDeshabilitada);
    }

    @PatchMapping("/{id}/tipo")
    public ResponseEntity<CuentaResponseDTO> actualizarTipoCuenta(
            @PathVariable @Positive @NotNull Long id,
            @RequestParam TipoCuenta tipo) {
        log.info("REST request to update Cuenta ID: {} to tipo: {}", id, tipo);
        CuentaResponseDTO cuentaActualizada = cuentaService.actualizarTipoCuenta(id, tipo);
        return ResponseEntity.ok(cuentaActualizada);
    }

    @PatchMapping("/{id}/cargar-saldo")
    public ResponseEntity<CuentaResponseDTO> cargarSaldo(
            @PathVariable @Positive @NotNull Long id,
            @RequestParam @Positive BigDecimal monto) {
        log.info("REST request to load saldo {} to Cuenta ID: {}", monto, id);
        CuentaResponseDTO cuentaActualizada = cuentaService.cargarSaldo(id, monto);
        return ResponseEntity.ok(cuentaActualizada);
    }

    @PatchMapping("/{id}/descontar-saldo")
    public ResponseEntity<CuentaResponseDTO> descontarSaldo(
            @PathVariable @Positive @NotNull Long id,
            @RequestParam @Positive BigDecimal monto) {
        log.info("REST request to deduct saldo {} from Cuenta ID: {}", monto, id);
        CuentaResponseDTO cuentaActualizada = cuentaService.descontarSaldo(id, monto);
        return ResponseEntity.ok(cuentaActualizada);
    }

    // Endpoint POST adicional para compatibilidad con Feign
    @PostMapping("/{id}/descontar-saldo")
    public ResponseEntity<CuentaResponseDTO> descontarSaldoPost(
            @PathVariable @Positive @NotNull Long id,
            @RequestParam @Positive BigDecimal monto) {
        log.info("REST request POST to deduct saldo {} from Cuenta ID: {}", monto, id);
        return descontarSaldo(id, monto);
    }

    @PatchMapping("/{id}/kilometros")
    public ResponseEntity<CuentaResponseDTO> actualizarKilometros(
            @PathVariable @Positive @NotNull Long id,
            @RequestParam @Positive Double kilometros) {
        log.info("REST request to update kilometros {} for Cuenta ID: {}", kilometros, id);
        CuentaResponseDTO cuentaActualizada = cuentaService.actualizarKilometros(id, kilometros);
        return ResponseEntity.ok(cuentaActualizada);
    }

    // Endpoint POST adicional para compatibilidad con Feign
    @PostMapping("/{id}/kilometros")
    public ResponseEntity<CuentaResponseDTO> actualizarKilometrosPost(
            @PathVariable @Positive @NotNull Long id,
            @RequestParam @Positive Double kilometros) {
        log.info("REST request POST to update kilometros {} for Cuenta ID: {}", kilometros, id);
        return actualizarKilometros(id, kilometros);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable @Positive @NotNull Long id) {
        log.info("REST request to delete Cuenta ID: {}", id);
        cuentaService.deleteCuenta(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/renovar-cupo")
    public ResponseEntity<Void> renovarCupoPremium(@PathVariable @Positive @NotNull Long id) {
        log.info("REST request to renew premium cupo for Cuenta ID: {}", id);
        cuentaService.renovarCupoPremium(id);
        return ResponseEntity.noContent().build();
    }
}
