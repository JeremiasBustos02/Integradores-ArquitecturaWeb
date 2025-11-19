package com.microservices.gateway.security;

import com.microservices.gateway.client.UsuarioFeignClient;
import com.microservices.gateway.dto.UsuarioAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("userDetailsService")
@RequiredArgsConstructor
@Slf4j
public class DomainUserDetailsService implements UserDetailsService {

    private final UsuarioFeignClient usuarioFeignClient;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        log.debug("Authenticating {}", username);

        try {
            UsuarioAuthDTO usuario = usuarioFeignClient.getUsuarioByEmailForAuth(username.toLowerCase());
            
            if (usuario == null) {
                throw new UsernameNotFoundException("El usuario " + username + " no existe");
            }

            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            if (usuario.getRol() != null) {
                grantedAuthorities.add(new SimpleGrantedAuthority(usuario.getRol()));
            }

            return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                grantedAuthorities
            );
        } catch (Exception e) {
            log.error("Error al buscar usuario: {}", e.getMessage(), e);
            throw new UsernameNotFoundException("El usuario " + username + " no existe", e);
        }
    }
}
