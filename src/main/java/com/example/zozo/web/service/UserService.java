package com.example.zozo.web.service;

import com.example.zozo.web.model.User;
import com.example.zozo.web.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /*public User getUsersById(Long id){
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElseGet(User::new);
    }*/
    public List<User> getUserByFirstName(String firstName) {
        return userRepository.findByFirstNameContaining(firstName);
    }
}
