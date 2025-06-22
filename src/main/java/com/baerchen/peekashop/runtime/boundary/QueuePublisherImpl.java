package com.baerchen.peekashop.runtime.boundary;

import com.baerchen.peekashop.operation.boundary.QueuePublisher;
import com.baerchen.peekashop.operation.entity.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

//@Service
@RequiredArgsConstructor
public class QueuePublisherImpl implements QueuePublisher {

    private final AmqpTemplate amqpTemplate;

  //  @Override
    public void publish(Operation operation) {
        amqpTemplate.convertAndSend(
                "peekashop.exchange",
                "operations.priority",
                operation
        );
    }

    /*
    rabbitTemplate.convertAndSend(
    "order.create.exchange", // exchange
    "order.create",          // routing key
    yourPayload,             // message object
    message -> {
        message.getMessageProperties().setPriority(yourPriorityValue); // 0 to MAX_PRIORITY
        return message;
    }
);

     */
}
