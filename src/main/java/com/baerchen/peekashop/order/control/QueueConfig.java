package com.baerchen.peekashop.order.control;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    public static final int MAX_PRIORITY = 100;
    private Queue buildQueue(String name, String dlq) {
        return QueueBuilder.durable(name)
                .withArgument("x-max-priority", MAX_PRIORITY)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", dlq)
                .build();
    }

    @Bean
    public Queue orderCreatePriorityQueue() {
        return buildQueue("order.create.priority", "order.create.priority.dlq");
    }

    @Bean
    public Queue orderCreateRealtimeQueue() {
        return buildQueue("order.create.realtime", "order.create.realtime.dlq");
    }

    @Bean
    public Queue orderCreateBulkQueue() {
        return buildQueue("order.create.bulk", "order.create.bulk.dlq");
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
    public DirectExchange orderExchange() {
        return new DirectExchange("order.exchange");
    }

    @Bean
    public Binding orderCreateBinding() {
        return BindingBuilder
                .bind(orderCreatePriorityQueue())
                .to(orderExchange())
                .with("order.create");
    }

    @Bean
    public Binding orderCreateRealtimeBinding() {
        return BindingBuilder
                .bind(orderCreateRealtimeQueue())
                .to(orderExchange())
                .with("order.create.realtime");
    }

    @Bean
    public Binding orderCreateBulkBinding() {
        return BindingBuilder
                .bind(orderCreateBulkQueue())
                .to(orderExchange())
                .with("order.create.bulk");
    }

}
