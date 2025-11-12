package com.microservices.usuarios.controller;

import com.microservices.usuarios.dto.request.UsuarioRequestDTO;
import com.microservices.usuarios.dto.response.CuentaResponseDTO;
import com.microservices.usuarios.dto.response.UsuarioResponseDTO;
import com.microservices.usuarios.service.IUsuarioService;
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

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;

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

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable @Positive @NotNull Long id) {
        log.info("REST request to get Usuario by ID: {}", id);
        UsuarioResponseDTO usuario = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioByEmail(@PathVariable String email) {
        log.info("REST request to get Usuario by email: {}", email);
        UsuarioResponseDTO usuario = usuarioService.getUsuarioByEmail(email);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/celular/{celular}")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioByCelular(@PathVariable String celular) {
        log.info("REST request to get Usuario by celular: {}", celular);
        UsuarioResponseDTO usuario = usuarioService.getUsuarioByCelular(celular);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> getAllUsuarios(Pageable pageable) {
        log.info("REST request to get all Usuarios");
        Page<UsuarioResponseDTO> usuarios = usuarioService.getAllUsuarios(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UsuarioResponseDTO>> searchUsuarios(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            Pageable pageable) {
        log.info("REST request to search Usuarios by name: {} {}", nombre, apellido);
        Page<UsuarioResponseDTO> usuarios = usuarioService.searchUsuariosByName(nombre, apellido, pageable);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<Page<UsuarioResponseDTO>> getUsuariosByCuentaId(
            @PathVariable @Positive @NotNull Long cuentaId,
            Pageable pageable) {
        log.info("REST request to get Usuarios by cuenta ID: {}", cuentaId);
        Page<UsuarioResponseDTO> usuarios = usuarioService.getUsuariosByCuentaId(cuentaId, pageable);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}/cuentas")
    public ResponseEntity<List<CuentaResponseDTO>> getCuentasByUsuarioId(
            @PathVariable @Positive @NotNull Long id) {
        log.info("REST request to get Cuentas for Usuario ID: {}", id);
        List<CuentaResponseDTO> cuentas = usuarioService.getCuentasByUsuarioId(id);
        return ResponseEntity.ok(cuentas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> updateUsuario(
            @PathVariable @Positive @NotNull Long id,
            @RequestBody @Valid UsuarioRequestDTO requestDTO) {
        log.info("REST request to update Usuario ID: {}", id);
        UsuarioResponseDTO updatedUsuario = usuarioService.updateUsuario(id, requestDTO);
        return ResponseEntity.ok(updatedUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable @Positive @NotNull Long id) {
        log.info("REST request to delete Usuario ID: {}", id);
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{usuarioId}/cuentas/{cuentaId}")
    public ResponseEntity<Void> asociarUsuarioACuenta(
            @PathVariable @Positive @NotNull Long usuarioId,
            @PathVariable @Positive @NotNull Long cuentaId) {
        log.info("REST request to associate Usuario ID: {} to Cuenta ID: {}", usuarioId, cuentaId);
        usuarioService.asociarUsuarioACuenta(usuarioId, cuentaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{usuarioId}/cuentas/{cuentaId}")
    public ResponseEntity<Void> desasociarUsuarioDeCuenta(
            @PathVariable @Positive @NotNull Long usuarioId,
            @PathVariable @Positive @NotNull Long cuentaId) {
        log.info("REST request to disassociate Usuario ID: {} from Cuenta ID: {}", usuarioId, cuentaId);
        usuarioService.desasociarUsuarioDeCuenta(usuarioId, cuentaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{usuarioId}/cuentas/cuenta-para-facturar")
    public ResponseEntity<Long> getCuentaParaFacturar(@PathVariable Long usuarioId) {
        Long idCuenta = usuarioService.getCuentaParaFacturar(usuarioId);
        return ResponseEntity.ok(idCuenta);
    }
}
