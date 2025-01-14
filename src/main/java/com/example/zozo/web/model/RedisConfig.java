package com.example.zozo.web.model;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class RedisConfig {

    @Bean
    public RedisClient redisClient() {
        return RedisClient.create("redis://localhost:6379");
    }

    @Bean
    public StatefulRedisConnection<String, String> connection(RedisClient redisClient) {
        return redisClient.connect();
    }

    @Bean
    public RedisCommands<String, String> syncCommands(StatefulRedisConnection<String, String> connection) {
        return connection.sync();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    public RedisScript<Long> rateLimitScript() {
        String script =
                "local current_count = redis.call('get', KEYS[1])\n" +
                        "if current_count and tonumber(current_count) >= tonumber(ARGV[1]) then\n" +
                        "    return 0\n" +
                        "else\n" +
                        "    if not current_count then\n" +  // If key doesn't exist, increment and set expiration
                        "        redis.call('incr', KEYS[1])\n" +
                        "        redis.call('expire', KEYS[1], ARGV[2])\n" +
                        "    else\n" +
                        "        redis.call('incr', KEYS[1])\n" +
                        "    end\n" +
                        "    return 1\n" +
                        "end";
        return RedisScript.of(script, Long.class);
    }
}
