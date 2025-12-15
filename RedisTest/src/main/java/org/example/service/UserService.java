package org.example.service;

import org.example.model.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Cacheable(cacheNames = "users", key = "#id")
    public UserDto getUserById(String id) {
        // Log entry to indicate method execution (only happens on cache miss)
        log.info("getUserById - cache miss, fetching for id={}", id);
        // Simulate slow call / DB fetch
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        UserDto dto = new UserDto(id, "User-" + id);
        log.debug("getUserById - fetched {}", dto);
        return dto;
    }

    @CacheEvict(cacheNames = "users", key = "#id")
    public void evictUser(String id) {
        log.info("evictUser - evicting cache for id={}", id);
        // no-op; cache eviction handled by annotation
    }
}
