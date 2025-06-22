package com.baerchen.peekashop.order.control;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rabbitmq.listener")
public class RabbitListenerProperties {
    private Listener standard = new Listener();
    private Listener dlq = new Listener();

    @Data
    public static class Listener {
        /** concurrency for consumers */
        private int concurrency = 1;
        /** maximum concurrent consumers */
        private int maxConcurrency = 1;
        /** prefetch count */
        private int prefetch = 10;
    }
}
