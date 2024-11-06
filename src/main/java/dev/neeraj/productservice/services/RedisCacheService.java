package dev.neeraj.productservice.services;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
@AllArgsConstructor
public class RedisCacheService implements CacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void put(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    @Override
    public Optional<Object> get(String key, String hashKey) {
        return Optional.ofNullable(redisTemplate.opsForHash().get(key, hashKey));
    }

    @Override
    public List<Object> values(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    @Override
    public void clear(String key) {
        redisTemplate.delete(key);
    }
}
