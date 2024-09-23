package com.example.zozo.web.controller;


import com.example.zozo.web.model.User;
import com.example.zozo.web.repository.UserRepository;
import com.example.zozo.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return userService.getUserByFirstName(firstName == null ? "" : firstName);
    }
    /*public User getUsersByName(@RequestParam(value = "id", required = false) Long id){
        return userService.getUsersById(id);
    }*/


    @Autowired
    private UserRepository userRepository;

    @PostMapping("/insert")
    public ResponseEntity<String> insertUser(@RequestBody User user) {
        user.setName(user.getFirstName() + " " + user.getLastName());
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return new ResponseEntity<>("User inserted failed", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>("User inserted successfully", HttpStatus.CREATED);
    }
}
