package com.baerchen.peekashop.operation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Operation {
    public Long id;
    public String type;
    public Long targetId;
    public int retryCount;
    public String lastError;
    public String status;
    public Instant createdAt;
    public int priority;
}