package com.example.spring_jwt_tutorial.controller;

import com.example.spring_jwt_tutorial.model.AuthRequest;
import com.example.spring_jwt_tutorial.model.User;
import com.example.spring_jwt_tutorial.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> welcomeMessage() {
        boolean authStatus = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        return ResponseEntity.ok("Welcome to Spring JWT!");
    }

    @GetMapping("/greetings")
    @PreAuthorize("hasAuthority('VIEW')")
    public ResponseEntity<?> greetings() {
        return ResponseEntity.ok("Hello World!");
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        userService.signUp(user);
        return ResponseEntity.ok().body("Sign up successful");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody @Validated AuthRequest authRequest) {
        return ResponseEntity.ok().body(userService.login(authRequest));
    }
}
