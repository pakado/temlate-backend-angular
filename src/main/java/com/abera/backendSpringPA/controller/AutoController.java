package com.abera.backendSpringPA.controller;

import com.abera.backendSpringPA.dto.LoginRequest;
import com.abera.backendSpringPA.dto.RegistryRequest;
import com.abera.backendSpringPA.model.User;
import com.abera.backendSpringPA.service.AuthService;
import com.abera.backendSpringPA.service.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AutoController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody RegistryRequest registryRequest) {
        authService.signup(registryRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return authService.getUsers();
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
