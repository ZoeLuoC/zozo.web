package com.example.zozo.web.controller;

import com.example.zozo.web.model.AuthResponse;
import com.example.zozo.web.model.LoginRequest;
import com.example.zozo.web.model.User;
import com.example.zozo.web.service.UserService;
import com.example.zozo.web.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest loginRequest){
        try{
            // Authenticate the user (using your own logic)
            //TODO: 1. get password from db; 2. verify password
            User user = userService.loadUserByEmail(loginRequest.getEmail());

            if(!loginRequest.getPassword().equals(user.getPassword())) {
            //if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Invalid username or password");
            }

            String token = JwtTokenUtil.generateToken(loginRequest.getEmail());

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Authentication failed");
        }

    }
}

