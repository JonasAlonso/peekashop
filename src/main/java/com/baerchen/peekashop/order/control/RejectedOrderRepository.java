package com.baerchen.peekashop.order.control;

import com.baerchen.peekashop.order.entity.RejectedOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RejectedOrderRepository  extends JpaRepository<RejectedOrderEntity, Long> {
}
