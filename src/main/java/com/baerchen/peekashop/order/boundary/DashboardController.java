package com.baerchen.peekashop.order.boundary;

import com.baerchen.peekashop.order.control.OrderMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class DashboardController {

        private final OrderMetricsService metrics;

        @GetMapping("/dashboard")
        public Map<String, Object> getDashboard() {
            return null;
           // return metrics.snapshot();
        }

}
