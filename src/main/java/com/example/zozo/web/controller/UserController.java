package com.example.zozo.web.controller;


import com.example.zozo.web.model.User;
import com.example.zozo.web.repository.UserRepository;
import com.example.zozo.web.service.UserService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/query")
    public List<User> getUsersByFirstName(@RequestParam(value = "firstName", required = false) String firstName){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // TODO: Query user info by email
        return userService.getUserByFirstName(firstName == null ? "" : firstName);
    }

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/insert")
    public ResponseEntity<String> insertUser(@RequestBody User user) {
        user.setName(user.getFirstName() + " " + user.getLastName());
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if(e.getCause() instanceof ConstraintViolationException) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists!");
            }
            return new ResponseEntity<>("User inserted failed", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>("User inserted successfully", HttpStatus.CREATED);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.loadUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(404).body("User not found");
            }
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching user data");
        }
    }

}
