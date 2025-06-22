package com.baerchen.peekashop.order.control;

import com.baerchen.peekashop.order.boundary.OrderService;
import com.baerchen.peekashop.order.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.List;
import java.util.UUID;


@SpringBootTest
class OrderIntegrationTests {

    @Autowired
    private OrderService orderService;

    @Test
    void givenAnOrder_ThenPersistItSuccessfully(){
        Order order = new Order();
        order.setCorrelationId(UUID.randomUUID());
        order.setUserId(42L);
        order.setItemIds(List.of(1L, 2L, 3L));
        order.setAmount(56.07);
        order.setCreatedAt(Instant.now());
        order.setPriority(3);

        this.orderService.createOrder(order);

        Order result = this.orderService.getOrder(1l);

       System.out.println(result);
    }

}