package com.baerchen.peekashop.order.boundary;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PriorityQueueConfig {

    public static final int MAX_PRIORITY = 100;

    // === Order Create ===
    @Bean
    public Queue orderCreatePriorityQueue() {
        return QueueBuilder.durable("order.create.priority")
                .withArgument("x-max-priority", MAX_PRIORITY)
                .withArgument("x-dead-letter-exchange", "")
                //.withArgument("x-message-ttl", 60000) // die after 60000 tries
                .withArgument("x-dead-letter-routing-key", "order.create.priority.dlq")
                .build();
    }

    @Bean
    public Queue orderCreateRealtimeQueue() {
        return QueueBuilder.durable("order.create.realtime")
                .withArgument("x-max-priority", MAX_PRIORITY)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "order.create.realtime.dlq")
                .build();
    }

    @Bean
    public Queue orderCreateBulkQueue() {
        return QueueBuilder.durable("order.create.bulk")
                .withArgument("x-max-priority", MAX_PRIORITY)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", "order.create.bulk.dlq")
                .build();
    }

    @Bean
    public Queue orderCreateRealtimeDlq() {
        return QueueBuilder.durable("order.create.realtime.dlq").build();
    }

    @Bean
    public Queue orderCreateBulkDlq() {
        return QueueBuilder.durable("order.create.bulk.dlq").build();
    }


    @Bean
    public Queue orderCreateDLQ() {
        return QueueBuilder.durable("order.create.priority.dlq").build();
    }

    @Bean
    public DirectExchange orderCreatePriorityExchange() {
        return new DirectExchange("order.create.exchange");
    }

    @Bean
    public Binding orderCreateBinding() {
        return BindingBuilder
                .bind(orderCreatePriorityQueue())
                .to(orderCreatePriorityExchange())
                .with("order.create");
    }

}
