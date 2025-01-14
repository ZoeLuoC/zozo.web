package com.example.zozo.web.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RateLimitService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisScript<Long> rateLimitScript;

    public RateLimitService(RedisTemplate<String, Object> redisTemplate, RedisScript<Long> rateLimitScript) {
        this.redisTemplate = redisTemplate;
        this.rateLimitScript = rateLimitScript;
    }

    public boolean isAllowed(String key, int limit, int windowInSeconds) {
        Long result = redisTemplate.execute(rateLimitScript, Collections.singletonList(key), limit, windowInSeconds);
        return result != null && result == 1;
    }
}
