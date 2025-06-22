package com.baerchen.peekashop.order.boundary;

import com.baerchen.peekashop.order.control.OrderMetricsService;
import com.baerchen.peekashop.order.entity.Order;
import com.baerchen.peekashop.order.entity.RequestTestParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

import static com.baerchen.peekashop.order.control.QueueConfig.MAX_PRIORITY;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderTestController {


    private final RabbitTemplate rabbitTemplate;

    private final OrderMetricsService metrics;


    @PostMapping("/orders")
    public String sendOrder(@RequestBody Order order) {
        if (order.getCorrelationId() == null) {
            order.setCorrelationId(UUID.randomUUID());
        }
        if (order.getCreatedAt() == null) {
            order.setCreatedAt(Instant.now());
        }
        rabbitTemplate.convertAndSend("order.create.exchange", "order.create", order);
        return "Order sent!";
    }

    @PostMapping("/test")
    public void simulate() {
        Order order;
        for (int i = 1; i <= 2; i++) {
            order = new Order();
            order.setAmount(new Random().nextDouble()*20);
            order.setCreatedAt(Instant.now());
            order.setPriority(new Random().nextInt(MAX_PRIORITY));
            order.setCorrelationId(UUID.randomUUID());
            order.setSource("bulk-" + order.getCorrelationId().toString());
            rabbitTemplate.convertAndSend("", "order.create.bulk",
                    order);
        }
        for (int i = 1; i <= 5; i++) {
            order = new Order();
            order.setAmount(new Random().nextDouble()*20);
            order.setCreatedAt(Instant.now());
            order.setPriority(new Random().nextInt(10));
            order.setCorrelationId(UUID.randomUUID());
            order.setSource("realtime-" + order.getCorrelationId().toString());
            rabbitTemplate.convertAndSend("", "order.create.realtime",
                    order);
        }
    }

    @PostMapping("/send-orders")
    public String sendBulk(@RequestBody RequestTestParams params){
        var random = new Random();
        long luck;
        Order order;
        String source = params.getType();
        String queue = params.getQueue().equals("realtime") ? "order.create.realtime" :"order.create.bulk";
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 1; i <= params.getTransactions(); i++) {
            order = new Order();
            order.setCreatedAt(Instant.now());
            order.setCorrelationId(UUID.randomUUID());
            order.setStatus("PREPARED");
            order.setAmount(new Random().nextDouble()*20);
            order.setSource(params.getType());
            //order.setPriority(random.nextInt(MAX_PRIORITY));
            log.info("Sending [{}] to [{}]", order.getCorrelationId(),queue);
            stringBuilder.append("Sending " + order.getCorrelationId() + " to " + queue + "\n");
            sendPriorityOrder(queue, order);
        }
        return stringBuilder.toString();
    }

    @PostMapping("/priority-test")
    public void testPriorities() {
        // Low-priority orders (should be delayed)
        var random = new Random();
        long luck;
        String queue;
        Order order;
        for (int i = 1; i <= 10000; i++) {
            luck = random.nextInt(20);
            order = new Order();
            order.setCreatedAt(Instant.now());
            order.setAmount(new Random().nextDouble()*20);

            order.setCorrelationId(UUID.randomUUID());

            if (luck==1){
                queue = "order.create.realtime";
                order.setSource("realtime");
            } else {
                order.setSource("bulk");
                queue = "order.create.bulk";
            }
            order.setPriority(random.nextInt(MAX_PRIORITY));
            sendPriorityOrder(queue, order);
        }
    }

    private void sendPriorityOrder(String queue, Order order) {
        long start = System.currentTimeMillis();
        if ("order.create.realtime".equals(queue)){
            rabbitTemplate.convertAndSend(
                    "", "order.create.realtime", order,
                    message -> {
                        message.getMessageProperties().setPriority(order.getPriority());
                        return message;
                    });
        } else {
            //Bulk is the default
            rabbitTemplate.convertAndSend(
                    "", "order.create.bulk", order,
                    message -> {
                        message.getMessageProperties().setPriority(order.getPriority());
                        return message;
                    });
        }
        metrics.logSuccess(order.getSource(), System.currentTimeMillis() - start);
    }

}
