package org.example.rest;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/cache")
public class CacheInspectorController {

    private final StringRedisTemplate redisTemplate;
    private final CacheManager cacheManager;

    public CacheInspectorController(StringRedisTemplate redisTemplate, CacheManager cacheManager) {
        this.redisTemplate = redisTemplate;
        this.cacheManager = cacheManager;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> inspectUserCache(@PathVariable String id) {
        String key = "users::" + id;
        Boolean exists = redisTemplate.hasKey(key);
        Object value = null;
        Long ttl = null;

        // Try to read the deserialized value from Spring Cache abstraction
        Cache cache = cacheManager.getCache("users");
        if (cache != null) {
            Cache.ValueWrapper vw = cache.get(id);
            if (vw != null) {
                value = vw.get();
            }
        }

        if (Boolean.TRUE.equals(exists)) {
            ttl = redisTemplate.getExpire(key);
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("key", key);
        resp.put("existsInRedis", exists);
        resp.put("deserializedValue", value);
        resp.put("ttlSeconds", ttl);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> listUserCacheKeys() {
        Set<String> keys = redisTemplate.keys("users*");
        Map<String, Object> resp = new HashMap<>();
        resp.put("keys", keys);
        return ResponseEntity.ok(resp);
    }
}
