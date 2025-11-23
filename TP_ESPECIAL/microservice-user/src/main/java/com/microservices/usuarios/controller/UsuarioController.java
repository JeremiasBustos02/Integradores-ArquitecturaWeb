package com.microservices.usuarios.controller;

import com.microservices.usuarios.dto.request.UsuarioRequestDTO;
import com.microservices.usuarios.dto.response.CuentaResponseDTO;
import com.microservices.usuarios.dto.response.UsuarioAuthResponseDTO;
import com.microservices.usuarios.dto.response.UsuarioResponseDTO;
import com.microservices.usuarios.dto.response.UsuarioUsoDTO;
import com.microservices.usuarios.service.IUsuarioService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api/usuarios")
@Tag(name = "Servicio de Usuarios", description = "Gestión de usuarios, roles y vinculación con cuentas")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    @Operation(summary = "Crear usuario", description = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> createUsuario(@RequestBody @Valid UsuarioRequestDTO requestDTO) {
        log.info("REST request to create Usuario: {}", requestDTO.getEmail());
        UsuarioResponseDTO createdUsuario = usuarioService.createUsuario(requestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/usuarios/{id}")
                .buildAndExpand(createdUsuario.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdUsuario);
    }

    @Operation(summary = "Obtener usuario por ID", description = "Busca un usuario específico por su identificador único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontró el usuario"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable @Positive @NotNull Long id) {
        log.info("REST request to get Usuario by ID: {}", id);
        UsuarioResponseDTO usuario = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Obtener usuario por Email", description = "Busca un usuario utilizando su dirección de correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontró usuario con ese email"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioByEmail(@PathVariable String email) {
        log.info("REST request to get Usuario by email: {}", email);
        UsuarioResponseDTO usuario = usuarioService.getUsuarioByEmail(email);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Obtener usuario para Auth (Email)", description = "Recupera datos sensibles (password) para procesos de autenticación interna")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos de autenticación obtenidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/email/{email}/auth")
    public ResponseEntity<UsuarioAuthResponseDTO> getUsuarioByEmailForAuth(@PathVariable String email) {
        log.info("REST request to get Usuario for auth by email: {}", email);
        UsuarioAuthResponseDTO usuario = usuarioService.getUsuarioByEmailForAuth(email);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Obtener usuario por Celular", description = "Busca un usuario utilizando su número de celular")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "No se encontró usuario con ese celular"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/celular/{celular}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioByCelular(@PathVariable String celular) {
        log.info("REST request to get Usuario by celular: {}", celular);
        UsuarioResponseDTO usuario = usuarioService.getUsuarioByCelular(celular);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Listar todos los usuarios", description = "Devuelve un listado paginado de todos los usuarios del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> getAllUsuarios(Pageable pageable) {
        log.info("REST request to get all Usuarios");
        Page<UsuarioResponseDTO> usuarios = usuarioService.getAllUsuarios(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Buscar usuarios", description = "Búsqueda de usuarios por nombre y/o apellido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultados de la búsqueda"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<UsuarioResponseDTO>> searchUsuarios(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            Pageable pageable) {
        log.info("REST request to search Usuarios by name: {} {}", nombre, apellido);
        Page<UsuarioResponseDTO> usuarios = usuarioService.searchUsuariosByName(nombre, apellido, pageable);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Usuarios por Cuenta", description = "Obtiene los usuarios vinculados a una cuenta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de usuarios obtenido"),
            @ApiResponse(responseCode = "404", description = "No se encontró la cuenta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<Page<UsuarioResponseDTO>> getUsuariosByCuentaId(
            @PathVariable @Positive @NotNull Long cuentaId,
            Pageable pageable) {
        log.info("REST request to get Usuarios by cuenta ID: {}", cuentaId);
        Page<UsuarioResponseDTO> usuarios = usuarioService.getUsuariosByCuentaId(cuentaId, pageable);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Cuentas de un Usuario", description = "Obtiene todas las cuentas asociadas a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de cuentas obtenido"),
            @ApiResponse(responseCode = "404", description = "No se encontró el usuario"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}/cuentas")
    public ResponseEntity<List<CuentaResponseDTO>> getCuentasByUsuarioId(
            @PathVariable @Positive @NotNull Long id) {
        log.info("REST request to get Cuentas for Usuario ID: {}", id);
        List<CuentaResponseDTO> cuentas = usuarioService.getCuentasByUsuarioId(id);
        return ResponseEntity.ok(cuentas);
    }

    @Operation(summary = "Actualizar usuario", description = "Modifica los datos personales de un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "No se encontró el usuario"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateUsuario(
            @PathVariable @Positive @NotNull Long id,
            @RequestBody @Valid UsuarioRequestDTO requestDTO) {
        log.info("REST request to update Usuario ID: {}", id);
        UsuarioResponseDTO updatedUsuario = usuarioService.updateUsuario(id, requestDTO);
        return ResponseEntity.ok(updatedUsuario);
    }

    @Operation(summary = "Eliminar usuario", description = "Eliminación física de un usuario del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el usuario"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable @Positive @NotNull Long id) {
        log.info("REST request to delete Usuario ID: {}", id);
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Asociar usuario a cuenta", description = "Vincula un usuario existente a una cuenta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Asociación exitosa"),
            @ApiResponse(responseCode = "404", description = "Usuario o cuenta no encontrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{usuarioId}/cuentas/{cuentaId}")
    public ResponseEntity<Void> asociarUsuarioACuenta(
            @PathVariable @Positive @NotNull Long usuarioId,
            @PathVariable @Positive @NotNull Long cuentaId) {
        log.info("REST request to associate Usuario ID: {} to Cuenta ID: {}", usuarioId, cuentaId);
        usuarioService.asociarUsuarioACuenta(usuarioId, cuentaId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desasociar usuario de cuenta", description = "Elimina el vínculo entre un usuario y una cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Desasociación exitosa"),
            @ApiResponse(responseCode = "404", description = "Usuario, cuenta o relación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{usuarioId}/cuentas/{cuentaId}")
    public ResponseEntity<Void> desasociarUsuarioDeCuenta(
            @PathVariable @Positive @NotNull Long usuarioId,
            @PathVariable @Positive @NotNull Long cuentaId) {
        log.info("REST request to disassociate Usuario ID: {} from Cuenta ID: {}", usuarioId, cuentaId);
        usuarioService.desasociarUsuarioDeCuenta(usuarioId, cuentaId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener cuenta para facturar", description = "Determina qué cuenta se debe usar para cobrar un viaje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID de cuenta obtenido"),
            @ApiResponse(responseCode = "404", description = "Usuario no tiene cuentas válidas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{usuarioId}/cuentas/cuenta-para-facturar")
    public ResponseEntity<Long> getCuentaParaFacturar(@PathVariable Long usuarioId) {
        Long idCuenta = usuarioService.getCuentaParaFacturar(usuarioId);
        return ResponseEntity.ok(idCuenta);
    }

    /**
     * e. Como administrador quiero ver los usuarios que más utilizan los monopatines
     */
    @Operation(summary = "Reporte de usuarios frecuentes", description = "Devuelve los usuarios con más uso de monopatines en un periodo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/reporte/usuarios-frecuentes")
    public ResponseEntity<List<UsuarioUsoDTO>> getUsuariosMasFrecuentes(
            @Parameter(description = "Fecha de inicio (YYYY-MM-DD)")

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @Parameter(description = "Fecha de fin (YYYY-MM-DD)")

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @Parameter(description = "Filtrar por tipo de cuenta (opcional)")

            @RequestParam(required = false) String tipoCuenta) {
        log.info("REST request to get usuarios frecuentes from {} to {}, tipo: {}", desde, hasta, tipoCuenta);
        List<UsuarioUsoDTO> usuarios = usuarioService.getUsuariosMasFrecuentes(desde, hasta, tipoCuenta);
        return ResponseEntity.ok(usuarios);
    }
}
