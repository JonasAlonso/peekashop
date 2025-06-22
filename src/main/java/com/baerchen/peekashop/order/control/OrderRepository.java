package com.baerchen.peekashop.order.control;

import com.baerchen.peekashop.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByCorrelationId(UUID correlationId);
}
