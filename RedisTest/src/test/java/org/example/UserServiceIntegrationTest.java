package org.example;

import org.example.model.UserDto;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class UserServiceIntegrationTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7.0.11-alpine").withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        String address = redis.getHost();
        Integer port = redis.getFirstMappedPort();
        registry.add("spring.redis.host", () -> address);
        registry.add("spring.redis.port", () -> port);
    }

    @Autowired
    private UserService userService;

    @Test
    void cachingShouldWork() {
        long start = System.currentTimeMillis();
        UserDto u1 = userService.getUserById("1");
        long first = System.currentTimeMillis() - start;

        long start2 = System.currentTimeMillis();
        UserDto u2 = userService.getUserById("1");
        long second = System.currentTimeMillis() - start2;

        assertThat(u1).isNotNull();
        assertThat(u2).isNotNull();
        assertThat(u1.getId()).isEqualTo(u2.getId());
        // second call should be faster because of cache (significantly)
        assertThat(second).isLessThan(first);
    }
}

