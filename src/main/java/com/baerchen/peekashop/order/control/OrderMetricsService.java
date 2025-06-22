package com.baerchen.peekashop.order.control;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;




@Service
@RequiredArgsConstructor
public class OrderMetricsService {

    private final MeterRegistry registry;

    public void logSuccess(String source, long durationMillis) {
        registry.counter("orders_processed_total", "source", source).increment();
        registry.timer("orders_processing_duration", "source", source).record(durationMillis, TimeUnit.MILLISECONDS);
    }

}
