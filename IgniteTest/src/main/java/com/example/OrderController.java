package com.example;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository repo;

    public OrderController(OrderRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public void create(@RequestBody Order order) {
        repo.save(order);
    }

    @GetMapping("/{orderId}/{customerId}")
    public Order get(@PathVariable Long orderId,
                     @PathVariable Long customerId) {
        return repo.find(orderId, customerId);
    }

    @GetMapping("/expensive")
    public List<Order> expensive(@RequestParam double min) {
        return repo.findExpensive(min);
    }
}
