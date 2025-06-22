package com.baerchen.peekashop.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders", indexes = {
        @Index(name = "idx_correlation_id", columnList = "correlationId")
})
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID correlationId;
    private Long userId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "item_id")
    private List<Long> itemIds;

    private Double amount;
    private int priority;
    private String source;
    private String status;
    private Instant createdAt;
    private long processingTime;

    public OrderEntity(Order order){
        this.amount =order.getAmount();
        this.userId = order.getUserId();
        this.correlationId = order.getCorrelationId();
        this.itemIds = order.getItemIds();
        this.priority = order.getPriority();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt() == null ? Instant.now() :order.getCreatedAt();
        this.source = order.getSource();
        this.processingTime = order.getProcessingTime();
    }
}
