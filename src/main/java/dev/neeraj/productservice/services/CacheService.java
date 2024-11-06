package dev.neeraj.productservice.services;

import java.util.List;
import java.util.Optional;

public interface CacheService {
    void put(String key, String hashKey, Object value);
    Optional<Object> get(String key, String hashKey);
    List<Object> values(String key);
    void clear(String key);
}
