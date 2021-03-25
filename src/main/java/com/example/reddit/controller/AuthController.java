package com.example.reddit.controller;


import com.example.reddit.dto.LoginRequest;
import com.example.reddit.dto.RegisterRequest;
import com.example.reddit.model.VerificationToken;
import com.example.reddit.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;


/*
we only use controller when we need to call methods in service, so controller is the agent of which we
rely upon when we need to perform some business logic that is implemented by service layer
 */

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){
        authService.signup(registerRequest);
        return new ResponseEntity<>("Registration successful", OK);
    }

    @GetMapping("/{token}")
    public ResponseEntity<String> verifyToken(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account verified", OK);
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        authService.login(loginRequest);
        return new ResponseEntity<>("Login Successful", OK);
    }
}
