package com.baerchen.peekashop.operation.boundary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j

public class OperationListener {

   // @RabbitListener(queues = "order.create.priority", containerFactory = "orderCreateListenerFactory")
    public void handleCreate(String message) {
        log.info("📥 Received order.create: {}", message);
        // TODO: Deserialize and process
    }

 //   @RabbitListener(queues = "order.cancel.priority", containerFactory = "orderCancelListenerFactory")
    public void handleCancel(String message) {
        log.info("📥 Received order.cancel: {}", message);
        // TODO: Deserialize and process
    }

   // @RabbitListener(queues = "order.create.priority.dlq")
    public void listenToDLQ(Message failedMessage) {
        // Log, retry, alert… whatever makes sense
    }



}
