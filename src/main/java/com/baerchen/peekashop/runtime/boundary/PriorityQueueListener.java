package com.baerchen.peekashop.runtime.boundary;

import com.baerchen.peekashop.operation.entity.Operation;
import com.baerchen.peekashop.order.boundary.PriorityQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
//@Component
public class PriorityQueueListener {
    /*
    @RabbitListen er(queues = PriorityQueueConfig.PRIORITY_QUEUE_NAME)
    public void consume(Operation operation) {
        log.info("🐇 Consumed operation with priority {}: {}", operation.getPriority(), operation);
        // TODO: add logic based on type/status/etc.
    }*/
}
