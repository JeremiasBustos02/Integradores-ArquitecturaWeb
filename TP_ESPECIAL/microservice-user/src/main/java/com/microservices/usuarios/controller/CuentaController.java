package com.microservices.usuarios.controller;

import com.microservices.usuarios.dto.request.CuentaRequestDTO;
import com.microservices.usuarios.dto.response.CuentaResponseDTO;
import com.microservices.usuarios.entity.TipoCuenta;
import com.microservices.usuarios.service.ICuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Servicio de Cuentas", description = "Gestión de cuentas de usuario, saldos y estados")
public class CuentaController {

    private final ICuentaService cuentaService;

    @Operation(summary = "Crear nueva cuenta", description = "Crea una cuenta asociada a un usuario de Mercado Pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la cuenta inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

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

    @Operation(summary = "Obtener cuenta por ID", description = "Busca una cuenta específica por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada"),
            @ApiResponse(responseCode = "404", description = "No se encontró la cuenta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> getCuentaById(@PathVariable @Positive @NotNull Long id) {
        log.info("REST request to get Cuenta by ID: {}", id);
        CuentaResponseDTO cuenta = cuentaService.getCuentaById(id);
        return ResponseEntity.ok(cuenta);
    }

    @Operation(summary = "Obtener cuenta por Mercado Pago ID", description = "Busca una cuenta usando el ID externo de Mercado Pago")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada"),
            @ApiResponse(responseCode = "404", description = "No se encontró la cuenta asociada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/mercado-pago/{idMercadoPago}")
    public ResponseEntity<CuentaResponseDTO> getCuentaByIdMercadoPago(@PathVariable @Positive @NotNull Long idMercadoPago) {
        log.info("REST request to get Cuenta by Mercado Pago ID: {}", idMercadoPago);
        CuentaResponseDTO cuenta = cuentaService.getCuentaByIdMercadoPago(idMercadoPago);
        return ResponseEntity.ok(cuenta);
    }

    @Operation(summary = "Listar todas las cuentas", description = "Devuelve un listado paginado de todas las cuentas del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Page<CuentaResponseDTO>> getAllCuentas(Pageable pageable) {
        log.info("REST request to get all Cuentas");
        Page<CuentaResponseDTO> cuentas = cuentaService.getAllCuentas(pageable);
        return ResponseEntity.ok(cuentas);
    }

    @Operation(summary = "Filtrar cuentas por estado", description = "Busca cuentas habilitadas (true) o deshabilitadas (false)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado filtrado obtenido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<CuentaResponseDTO>> getCuentasByEstado(
            @PathVariable Boolean estado,
            Pageable pageable) {
        log.info("REST request to get Cuentas by estado: {}", estado);
        Page<CuentaResponseDTO> cuentas = cuentaService.getCuentasByEstado(estado, pageable);
        return ResponseEntity.ok(cuentas);
    }

    @Operation(summary = "Filtrar cuentas por tipo", description = "Busca cuentas de un tipo específico (BASICA, PREMIUM)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado filtrado obtenido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Page<CuentaResponseDTO>> getCuentasByTipo(
            @Parameter(description = "Tipo de cuenta (ej: BASICA, PREMIUM)")
            @PathVariable TipoCuenta tipo,
            Pageable pageable) {
        log.info("REST request to get Cuentas by tipo: {}", tipo);
        Page<CuentaResponseDTO> cuentas = cuentaService.getCuentasByTipo(tipo, pageable);
        return ResponseEntity.ok(cuentas);
    }

    @Operation(summary = "Buscar cuentas de un usuario", description = "Obtiene todas las cuentas asociadas a un usuario específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuentas encontradas"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<CuentaResponseDTO>> getCuentasByUsuarioId(
            @PathVariable @Positive @NotNull Long usuarioId,
            Pageable pageable) {
        log.info("REST request to get Cuentas by usuario ID: {}", usuarioId);
        Page<CuentaResponseDTO> cuentas = cuentaService.getCuentasByUsuarioId(usuarioId, pageable);
        return ResponseEntity.ok(cuentas);
    }

    @Operation(summary = "Habilitar cuenta", description = "Reactiva una cuenta previamente deshabilitada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta habilitada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró la cuenta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/habilitar")
    public ResponseEntity<CuentaResponseDTO> habilitarCuenta(@PathVariable @Positive @NotNull Long id) {
        log.info("REST request to enable Cuenta ID: {}", id);
        CuentaResponseDTO cuentaHabilitada = cuentaService.habilitarCuenta(id);
        return ResponseEntity.ok(cuentaHabilitada);
    }

    @Operation(summary = "Deshabilitar cuenta", description = "Suspende temporalmente una cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta deshabilitada correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró la cuenta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/deshabilitar")
    public ResponseEntity<CuentaResponseDTO> deshabilitarCuenta(@PathVariable @Positive @NotNull Long id) {
        log.info("REST request to disable Cuenta ID: {}", id);
        CuentaResponseDTO cuentaDeshabilitada = cuentaService.deshabilitarCuenta(id);
        return ResponseEntity.ok(cuentaDeshabilitada);
    }

    @Operation(summary = "Cambiar tipo de cuenta", description = "Actualiza el nivel de suscripción de la cuenta (Upgrade/Downgrade)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de cuenta actualizado"),
            @ApiResponse(responseCode = "404", description = "No se encontró la cuenta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/tipo")
    public ResponseEntity<CuentaResponseDTO> actualizarTipoCuenta(
            @PathVariable @Positive @NotNull Long id,
            @RequestParam TipoCuenta tipo) {
        log.info("REST request to update Cuenta ID: {} to tipo: {}", id, tipo);
        CuentaResponseDTO cuentaActualizada = cuentaService.actualizarTipoCuenta(id, tipo);
        return ResponseEntity.ok(cuentaActualizada);
    }

    @Operation(summary = "Cargar saldo", description = "Incrementa el saldo disponible en la cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saldo cargado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Monto inválido (debe ser positivo)"),
            @ApiResponse(responseCode = "404", description = "No se encontró la cuenta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/cargar-saldo")
    public ResponseEntity<CuentaResponseDTO> cargarSaldo(
            @PathVariable @Positive @NotNull Long id,
            @Parameter(description = "Monto a acreditar (positivo)")

            @RequestParam @Positive BigDecimal monto) {
        log.info("REST request to load saldo {} to Cuenta ID: {}", monto, id);
        CuentaResponseDTO cuentaActualizada = cuentaService.cargarSaldo(id, monto);
        return ResponseEntity.ok(cuentaActualizada);
    }

    @Operation(summary = "Descontar saldo", description = "Resta saldo de la cuenta (Pago de viajes/servicios)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saldo descontado correctamente"),
            @ApiResponse(responseCode = "400", description = "Saldo insuficiente o monto inválido"),
            @ApiResponse(responseCode = "404", description = "No se encontró la cuenta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/descontar-saldo")
    public ResponseEntity<CuentaResponseDTO> descontarSaldo(
            @PathVariable @Positive @NotNull Long id,
            @Parameter(description = "Monto a debitar (positivo)")

            @RequestParam @Positive BigDecimal monto) {
        log.info("REST request to deduct saldo {} from Cuenta ID: {}", monto, id);
        CuentaResponseDTO cuentaActualizada = cuentaService.descontarSaldo(id, monto);
        return ResponseEntity.ok(cuentaActualizada);
    }

    // Endpoint POST adicional para compatibilidad con Feign
    @Operation(summary = "Descontar saldo (Interno)", description = "Endpoint POST alternativo para clientes Feign")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saldo descontado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{id}/descontar-saldo")
    public ResponseEntity<CuentaResponseDTO> descontarSaldoPost(
            @PathVariable @Positive @NotNull Long id,
            @RequestParam @Positive BigDecimal monto) {
        log.info("REST request POST to deduct saldo {} from Cuenta ID: {}", monto, id);
        return descontarSaldo(id, monto);
    }

    @Operation(summary = "Actualizar kilómetros", description = "Suma kilómetros recorridos al historial de la cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kilómetros actualizados"),
            @ApiResponse(responseCode = "404", description = "No se encontró la cuenta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/kilometros")
    public ResponseEntity<CuentaResponseDTO> actualizarKilometros(
            @PathVariable @Positive @NotNull Long id,
            @Parameter(description = "Cantidad de kilómetros a sumar")

            @RequestParam @Positive Double kilometros) {
        log.info("REST request to update kilometros {} for Cuenta ID: {}", kilometros, id);
        CuentaResponseDTO cuentaActualizada = cuentaService.actualizarKilometros(id, kilometros);
        return ResponseEntity.ok(cuentaActualizada);
    }

    // Endpoint POST adicional para compatibilidad con Feign
    @Operation(summary = "Actualizar kilómetros (Interno)", description = "Endpoint POST alternativo para clientes Feign")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kilómetros actualizados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{id}/kilometros")
    public ResponseEntity<CuentaResponseDTO> actualizarKilometrosPost(
            @PathVariable @Positive @NotNull Long id,
            @RequestParam @Positive Double kilometros) {
        log.info("REST request POST to update kilometros {} for Cuenta ID: {}", kilometros, id);
        return actualizarKilometros(id, kilometros);
    }

    @Operation(summary = "Eliminar cuenta", description = "Eliminación física de la cuenta del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cuenta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró la cuenta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable @Positive @NotNull Long id) {
        log.info("REST request to delete Cuenta ID: {}", id);
        cuentaService.deleteCuenta(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Renovar cupo Premium", description = "Reinicia los contadores mensuales para cuentas Premium")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cupo renovado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró la cuenta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{id}/renovar-cupo")
    public ResponseEntity<Void> renovarCupoPremium(@PathVariable @Positive @NotNull Long id) {
        log.info("REST request to renew premium cupo for Cuenta ID: {}", id);
        cuentaService.renovarCupoPremium(id);
        return ResponseEntity.noContent().build();
    }
}
