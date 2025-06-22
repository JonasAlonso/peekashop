package com.baerchen.peekashop.operation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

public class OperationEntity {
    @Id
 //   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private Long targetId;
    private int retryCount;
    private String lastError;
    private String status;
    private Instant createdAt;
}
