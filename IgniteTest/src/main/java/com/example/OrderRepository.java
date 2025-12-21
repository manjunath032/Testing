package com.example;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.transactions.Transaction;
import org.springframework.stereotype.Repository;

import javax.cache.Cache;
import java.util.List;

@Repository
public class OrderRepository {

    private final IgniteCache<AffinityKey<Long>, Order> cache;
    private final Ignite ignite;

    public OrderRepository(Ignite ignite,
                           CacheConfiguration<AffinityKey<Long>, Order> cfg) {
        this.ignite = ignite;
        this.cache = ignite.getOrCreateCache(cfg);
    }

    public void save(Order order) {
        AffinityKey<Long> key =
                new AffinityKey<>(order.getOrderId(), order.getCustomerId());

        try (Transaction tx = ignite.transactions().txStart()) {
            cache.put(key, order);
            tx.commit();
        }
    }

    public Order find(Long orderId, Long customerId) {
        return cache.get(new AffinityKey<>(orderId, customerId));
    }

    public List<Order> findExpensive(double minAmount) {
        SqlQuery<AffinityKey<Long>, Order> query =
                new SqlQuery<>(Order.class, "amount > ?");

        query.setArgs(minAmount);

        return cache.query(query)
                .getAll()
                .stream()
                .map(Cache.Entry::getValue)
                .toList();
    }
}
