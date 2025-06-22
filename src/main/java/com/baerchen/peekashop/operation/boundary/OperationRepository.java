package com.baerchen.peekashop.operation.boundary;
import com.baerchen.peekashop.operation.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
}
