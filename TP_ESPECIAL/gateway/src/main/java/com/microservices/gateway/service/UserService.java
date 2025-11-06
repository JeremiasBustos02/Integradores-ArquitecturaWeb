package com.microservices.gateway.service;

import com.microservices.gateway.entity.Authority;
import com.microservices.gateway.entity.User;
import com.microservices.gateway.repository.AuthorityRepository;
import com.microservices.gateway.repository.UserRepository;
import com.microservices.gateway.security.AuthorityConstant;
import com.microservices.gateway.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    public User createUser(UserDTO userDTO, String password) {
        User user = new User();
        user.setUsername(userDTO.getUsername().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(password));
        user.setActivated(true);
        
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthorityConstant.USER).ifPresent(authorities::add);
        user.setAuthorities(authorities);
        
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .map(this::userToUserDTO);
    }

    private UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setActivated(user.isActivated());
        userDTO.setAuthorities(user.getAuthorities().stream()
                .map(Authority::getName)
                .collect(Collectors.toSet()));
        return userDTO;
    }
}
