package com.baerchen.peekashop.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order implements Serializable {
    private UUID correlationId;
    private Long userId;
    private List<Long> itemIds;
    private Double amount;
    private int priority;
    private String source;
    private String status;
    private Instant createdAt;
    private String issues;
    private long processingTime;

    //private Long id;

    public Order(OrderEntity orderEntity){
        this.status =orderEntity.getStatus();
        this.amount = orderEntity.getAmount();
        this.priority = orderEntity.getPriority();
        this.userId = orderEntity.getUserId();
        this.createdAt = orderEntity.getCreatedAt();
        this.itemIds = orderEntity.getItemIds();
        this.correlationId = orderEntity.getCorrelationId();
        this.source = orderEntity.getSource();
        this.processingTime = orderEntity.getProcessingTime();
    }
}