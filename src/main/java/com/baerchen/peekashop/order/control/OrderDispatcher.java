package com.baerchen.peekashop.order.control;

import com.baerchen.peekashop.order.boundary.OrderService;
import com.baerchen.peekashop.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OrderDispatcher {

    private final OrderService orderService;

        public void process(Order order, boolean isBulk) {
            if (isBulk) {
                orderService.createOrder(order);
                log.info("🐌 [BULK] Processing finished: {}", order.getCorrelationId());
            } else {
                orderService.createOrder(order);
                log.info("🚀 [REALTIME] Processing finished: {}", order.getCorrelationId());
            }
        }

}
