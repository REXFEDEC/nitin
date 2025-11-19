package com.nitin.secure_notes.controller;

import com.nitin.secure_notes.dto.AuthResponse;
import com.nitin.secure_notes.dto.LoginRequest;
import com.nitin.secure_notes.dto.RegisterRequest;
import com.nitin.secure_notes.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class AuthController {
    @Autowired
    private  UserService userService1;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register (@RequestBody @Valid RegisterRequest request){
        AuthResponse response = userService1.register(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request){
        AuthResponse response = userService1.login(request);
        return ResponseEntity.ok(response);
    }


}
