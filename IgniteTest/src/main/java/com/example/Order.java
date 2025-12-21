package com.example;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @QuerySqlField(index = true)
    private Long orderId;

    @QuerySqlField(index = true)
    private Long customerId;

    @QuerySqlField
    private Double amount;

}
