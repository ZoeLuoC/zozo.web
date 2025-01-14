package com.example.zozo.web.controller.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {
    int limit(); // Max number of requests
    int window(); // Time window in seconds
    String name(); // Unique name for the API endpoint
}
