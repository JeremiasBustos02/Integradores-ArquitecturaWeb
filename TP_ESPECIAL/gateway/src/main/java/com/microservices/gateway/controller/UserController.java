package com.microservices.gateway.controller;

import com.microservices.gateway.service.UserService;
import com.microservices.gateway.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/usuarios")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO, @RequestParam String password) {
        log.debug("REST request to save User : {}", userDTO);
        
        var newUser = userService.createUser(userDTO, password);
        var result = userService.getUserByUsername(newUser.getUsername());
        
        return result.map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/usuarios/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String username) {
        log.debug("REST request to get User : {}", username);
        return userService.getUserByUsername(username)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }
}
