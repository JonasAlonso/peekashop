package com.baerchen.peekashop.order.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitListenerFactory {



    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory standardListenerFactory(ConnectionFactory connectionFactory){
        log.info("creating standardListenerFactory");
        return getSimpleRabbitListenerContainerFactory(connectionFactory, 1, 1, 10);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory dlqListenerFactory(ConnectionFactory connectionFactory){
        log.info("creating dlqListenerFactory");

        return getSimpleRabbitListenerContainerFactory(connectionFactory, 1, 1, 10);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory orderCreateListenerFactory(ConnectionFactory connectionFactory) {
        log.info("creating orderCreateListenerFactory");

        return getSimpleRabbitListenerContainerFactory(connectionFactory, 3, 10, 5);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        log.info("creating rabbitListenerContainerFactory");

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setErrorHandler(t -> log.error("🐛 Message listener error", t));
        return factory;
    }
    private static SimpleRabbitListenerContainerFactory getSimpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory, int concurrency, int concurrency1, int prefetch) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(concurrency);
        factory.setMaxConcurrentConsumers(concurrency1);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setPrefetchCount(prefetch);
        return factory;
    }

    @Bean(name = "priorityListenerFactory")
    public SimpleRabbitListenerContainerFactory priorityListenerFactory(ConnectionFactory connectionFactory){
        log.info("creating priorityListenerFactory");
        var factory = getSimpleRabbitListenerContainerFactory(connectionFactory,3,10,5);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    @Bean("realtimeContainerFactory")
    public SimpleRabbitListenerContainerFactory realtimeFactory(ConnectionFactory connectionFactory) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        return factory;
    }

    @Bean("bulkContainerFactory")
    public SimpleRabbitListenerContainerFactory bulkFactory(ConnectionFactory connectionFactory) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(2);
        return factory;
    }

}
