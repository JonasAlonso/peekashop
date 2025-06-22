package com.baerchen.peekashop.order.boundary;

import com.baerchen.peekashop.order.entity.Order;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface OrderService {

    void createOrder(Order order);
    Order getOrder(Long id);

    void cancelOrder(Long id);

    void rejectOrder(UUID correlationID);
}
