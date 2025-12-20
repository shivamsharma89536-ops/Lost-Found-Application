package com.Android.Android_App.Controller;

import com.Android.Android_App.Dto.AuthResponse;
import com.Android.Android_App.Dto.LoginRequest;
import com.Android.Android_App.Dto.RegisterRequest;
import com.Android.Android_App.Entity.User;
import com.Android.Android_App.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthApiController {

    private final AuthService authService;

    public AuthApiController(AuthService authService) {
        this.authService = authService;
    }

    // Matches POST /auth/register
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // Matches POST /auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    


    // TODO: Add PATCH /users/me for updating profile
}