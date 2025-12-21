package com.example;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public CacheConfiguration<AffinityKey<Long>, Order> orderCacheConfig() {

        CacheConfiguration<AffinityKey<Long>, Order> cfg =
                new CacheConfiguration<>("order-cache");

        cfg.setBackups(1);
        cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        cfg.setIndexedTypes(AffinityKey.class, Order.class);

        return cfg;
    }
}
