package com.baerchen.peekashop.order.boundary;

import com.baerchen.peekashop.order.control.OrderDispatcher;
import com.baerchen.peekashop.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class OrderQueueListener {

    private final OrderService orderService;

    private final OrderDispatcher dispatcher;

    @RabbitListener(
            queues = "order.create.priority",
            containerFactory = "priorityListenerFactory"
    )
    public void handlePriorityOrder(Order order){
        log.info("📦 Handling (create) priority order: {}", order.getCorrelationId());
        orderService.createOrder(order);
    }
    @RabbitListener(
            queues = "order.create.priority.dlq",
            containerFactory = "dlqListenerFactory"
    )
    public void handleDlqPriorityMessage(Order order) {
            log.warn("☠️ Handling DLQ Order: {}", order);
            try {
            orderService.rejectOrder(order.getCorrelationId());
        } catch (Exception e) {
            log.error("💥 Failed to process DLQ message: {}", order, e);
        }
    }


    @RabbitListener(queues = "order.create.realtime", containerFactory = "realtimeContainerFactory")
    public void handleRealtime(Order order) throws InterruptedException {
        log.info("⚡ [REALTIME] Received order: {}", order.getCorrelationId());
        log.info("⚡ [REALTIME] [{}] Handling: {}", Thread.currentThread().getName(), order.getCorrelationId());
        Thread.sleep(4_000); // 5
        dispatcher.process(order, false);
    }

    @RabbitListener(queues = "order.create.bulk", containerFactory = "bulkContainerFactory")
    public void handleBulk(Order order) throws InterruptedException {
        log.info("🐢 [BULK] Received order: {}", order.getCorrelationId());
        log.info("🐢 [BULK] [{}] Handling: {}", Thread.currentThread().getName(), order.getCorrelationId());
        // Simulate absurd delay
        Thread.sleep(20_000); // 60 seconds
        dispatcher.process(order, true);
    }

    @RabbitListener(queues = "order.create.realtime.dlq",
            containerFactory = "dlqListenerFactory"
    )
    public void handleRealtimeDlq(Order failedOrder) {
        log.warn("💀 Realtime DLQ: {}", failedOrder);
    }

    @RabbitListener(queues = "order.create.bulk.dlq",
            containerFactory = "dlqListenerFactory"
    )
    public void handleBulkDlq(Order failedOrder) {
        log.warn("💀 Bulk DLQ: {}", failedOrder);
    }

}
