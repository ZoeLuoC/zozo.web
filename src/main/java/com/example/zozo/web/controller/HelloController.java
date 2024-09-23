package com.example.zozo.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Greeting from Cai Luo!";
    }

    @GetMapping("/husband")
    public String husband() {
        return "Greeting from Lingda Li!";
    }

}
