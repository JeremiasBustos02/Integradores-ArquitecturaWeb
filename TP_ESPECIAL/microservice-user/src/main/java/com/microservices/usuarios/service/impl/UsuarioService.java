package com.microservices.usuarios.service.impl;

import com.microservices.usuarios.dto.request.UsuarioRequestDTO;
import com.microservices.usuarios.dto.response.CuentaResponseDTO;
import com.microservices.usuarios.dto.response.UsuarioResponseDTO;
import com.microservices.usuarios.entity.Cuenta;
import com.microservices.usuarios.entity.TipoCuenta;
import com.microservices.usuarios.entity.Usuario;
import com.microservices.usuarios.exception.CuentaNotFoundException;
import com.microservices.usuarios.exception.DuplicateResourceException;
import com.microservices.usuarios.exception.UsuarioNotFoundException;
import com.microservices.usuarios.mapper.CuentaMapper;
import com.microservices.usuarios.mapper.UsuarioMapper;
import com.microservices.usuarios.repository.CuentaRepository;
import com.microservices.usuarios.repository.UsuarioRepository;
import com.microservices.usuarios.service.IUsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;
    private final UsuarioMapper usuarioMapper;
    private final CuentaMapper cuentaMapper;

    @Override
    public UsuarioResponseDTO createUsuario(UsuarioRequestDTO requestDTO) {
        log.info("Creando usuario con email: {}", requestDTO.getEmail());

        // Validar que no exista un usuario con el mismo email
        if (usuarioRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con el email: " + requestDTO.getEmail());
        }

        // Validar que no exista un usuario con el mismo celular
        if (usuarioRepository.existsByCelular(requestDTO.getCelular())) {
            throw new DuplicateResourceException("Ya existe un usuario con el celular: " + requestDTO.getCelular());
        }

        Usuario usuario = usuarioMapper.toEntity(requestDTO);
        Usuario savedUsuario = usuarioRepository.save(usuario);

        log.info("Usuario creado exitosamente con ID: {}", savedUsuario.getId());
        return usuarioMapper.toResponseDTO(savedUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO getUsuarioById(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO getUsuarioByEmail(String email) {
        log.info("Buscando usuario con email: {}", email);
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con email: " + email));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO getUsuarioByCelular(String celular) {
        log.info("Buscando usuario con celular: {}", celular);
        Usuario usuario = usuarioRepository.findByCelular(celular)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con celular: " + celular));
        return usuarioMapper.toResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> getAllUsuarios(Pageable pageable) {
        log.info("Obteniendo todos los usuarios - página: {}, tamaño: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        return usuarios.map(usuarioMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> searchUsuariosByName(String nombre, String apellido, Pageable pageable) {
        log.info("Buscando usuarios por nombre: {} y apellido: {}", nombre, apellido);
        Page<Usuario> usuarios = usuarioRepository.findByNombreOrApellidoContaining(nombre, apellido, pageable);
        return usuarios.map(usuarioMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> getUsuariosByCuentaId(Long cuentaId, Pageable pageable) {
        log.info("Obteniendo usuarios de la cuenta ID: {}", cuentaId);
        Page<Usuario> usuarios = usuarioRepository.findByCuentaId(cuentaId, pageable);
        return usuarios.map(usuarioMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaResponseDTO> getCuentasByUsuarioId(Long usuarioId) {
        log.info("Obteniendo cuentas del usuario ID: {}", usuarioId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));

        List<CuentaResponseDTO> cuentas = usuario.getCuentas().stream()
                .map(cuentaMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.info("Se encontraron {} cuentas para el usuario ID: {}", cuentas.size(), usuarioId);
        return cuentas;
    }

    @Override
    public Long getCuentaParaFacturar(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));

        Set<Cuenta> cuentas = usuario.getCuentas();

        if (cuentas == null || cuentas.isEmpty()) {
            throw new CuentaNotFoundException("El usuario no tiene cuentas asociadas.");
        }


        Optional<Cuenta> cuentaPremium = cuentas.stream()
                .filter(c -> c.getTipoCuenta() == TipoCuenta.PREMIUM)
                .findFirst();
        //devolver la primera cuenta premium
        if (cuentaPremium.isPresent()) {
            return cuentaPremium.get().getId();
        }
        //buscamos otra basica
        Optional<Cuenta> cuentaConSaldo = cuentas.stream()
                .filter(c -> c.getSaldo() != null && c.getSaldo().compareTo(BigDecimal.ZERO) > 0)
                .findFirst();

        if (cuentaConSaldo.isPresent()) {
            return cuentaConSaldo.get().getId();
        }

        // 3. Si no hay ninguna, falla
        throw new CuentaNotFoundException("El usuario no tiene ninguna cuenta Premium activa ni cuentas con saldo para facturar.");
    }

    @Override
    public UsuarioResponseDTO updateUsuario(Long id, UsuarioRequestDTO requestDTO) {
        log.info("Actualizando usuario con ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        // Validar email único si cambió
        if (!usuario.getEmail().equals(requestDTO.getEmail()) &&
                usuarioRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con el email: " + requestDTO.getEmail());
        }

        // Validar celular único si cambió
        if (!usuario.getCelular().equals(requestDTO.getCelular()) &&
                usuarioRepository.existsByCelular(requestDTO.getCelular())) {
            throw new DuplicateResourceException("Ya existe un usuario con el celular: " + requestDTO.getCelular());
        }

        usuario.setNombre(requestDTO.getNombre());
        usuario.setApellido(requestDTO.getApellido());
        usuario.setCelular(requestDTO.getCelular());
        usuario.setEmail(requestDTO.getEmail());

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        log.info("Usuario actualizado exitosamente con ID: {}", updatedUsuario.getId());
        return usuarioMapper.toResponseDTO(updatedUsuario);
    }

    @Override
    public void deleteUsuario(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        // Desasociar de todas las cuentas antes de eliminar
        usuario.getCuentas().forEach(cuenta -> cuenta.removeUsuario(usuario));

        usuarioRepository.delete(usuario);
        log.info("Usuario eliminado exitosamente con ID: {}", id);
    }

    @Override
    public void asociarUsuarioACuenta(Long usuarioId, Long cuentaId) {
        log.info("Asociando usuario ID: {} a cuenta ID: {}", usuarioId, cuentaId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));

        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new CuentaNotFoundException(cuentaId));

        cuenta.addUsuario(usuario);
        cuentaRepository.save(cuenta);

        log.info("Usuario asociado exitosamente a la cuenta");
    }

    @Override
    public void desasociarUsuarioDeCuenta(Long usuarioId, Long cuentaId) {
        log.info("Desasociando usuario ID: {} de cuenta ID: {}", usuarioId, cuentaId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));

        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new CuentaNotFoundException(cuentaId));

        cuenta.removeUsuario(usuario);
        cuentaRepository.save(cuenta);

        log.info("Usuario desasociado exitosamente de la cuenta");
    }


}
