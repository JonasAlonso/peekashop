package com.baerchen.peekashop.order.control;

import com.baerchen.peekashop.order.boundary.OrderService;
import com.baerchen.peekashop.order.entity.Order;
import com.baerchen.peekashop.order.entity.OrderEntity;
import com.baerchen.peekashop.order.entity.RejectedOrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final RejectedOrderRepository rejectedOrderRepository;

    @Override
    @Transactional
    public void createOrder(Order order) {
        log.info("📥 Incoming order: {}", order);

        // 💥 Validate & DLQ trigger simulation
        if (order.getAmount() == null || order.getAmount() > 100) {
            log.error("🚫 Invalid or expensive order. Sending to DLQ: {}", order);
            throw new RuntimeException("Wrong amount");
        }
        if (orderRepository.existsByCorrelationId(order.getCorrelationId())) {
            log.warn("🛑 Duplicate order received: {}", order.getCorrelationId());
            return;
        }
        Duration waitTime = Duration.between(order.getCreatedAt(), Instant.now());
        order.setProcessingTime(waitTime.toSeconds());
        order.setIssues("Processed by " + Thread.currentThread().getName());
        order.setStatus("PROCESSED");
        OrderEntity entity = new OrderEntity(order);
        try {
            orderRepository.save(entity);
        } catch (DataIntegrityViolationException exception){
            log.warn("order [{}] already persisted.",order.getCorrelationId());
            return;
        }
        log.info("⏱ Order-{} {} waited {} s in the queue",order.getSource(), order.getCorrelationId(), entity.getProcessingTime());
        log.info("✅ Order persisted: {}", entity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .map(Order::new)
                .orElse(null);
    }

    @Override
    public void cancelOrder(Long id) {
        var orderEntity = this.orderRepository.findById(id).orElseThrow();
        orderEntity.setStatus("CANCELED");
        orderRepository.save(orderEntity);
        log.info("Order [{}] cancelled.", id);
    }

    @Override
    public void rejectOrder(UUID correlationID){
        rejectedOrderRepository.save( RejectedOrderEntity.builder()
                .correlationID(correlationID)
                .rejectedAt(Instant.now())
                .build());
        log.info("Order [{}] was rejected.", correlationID);
    }
}
