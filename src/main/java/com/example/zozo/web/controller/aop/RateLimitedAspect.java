package com.example.zozo.web.controller.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;

@Aspect
@Component
public class RateLimitedAspect {

    private final RedisScript<Long> rateLimitScript;
    private final RedisTemplate<String, Object> redisTemplate;

    public RateLimitedAspect(RedisScript<Long> rateLimitScript, RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.rateLimitScript = rateLimitScript;
    }

    @Pointcut("@annotation(com.example.zozo.web.controller.aop.RateLimited)")
    public void rateLimitedMethods() {}

    //1. applyRateLimit -
    //2. rateLimitedMethods() -> @RateLimited
    @Before("rateLimitedMethods()")
    public void applyRateLimit(JoinPoint joinPoint) {
        // Get method and annotation details
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimited rateLimited = method.getAnnotation(RateLimited.class);

        if (rateLimited != null) {
            // Retrieve limit, window, and name from the annotation
            int limit = rateLimited.limit();
            int windowInSeconds = rateLimited.window();
            String name = rateLimited.name();

            // Create a unique key for each API endpoint based on the name
            String key = "rate_limit:" + name;
            Long result = redisTemplate.execute(rateLimitScript, Collections.singletonList(key), limit, windowInSeconds);
            if(result == null || result == 0) {
                throw new RuntimeException("Rate limit exceeded for API: " + name);//
            }
        }

        System.out.println("AOP triggered!!!" );
        System.out.println("TPS:" + rateLimited.limit());
    }
}
