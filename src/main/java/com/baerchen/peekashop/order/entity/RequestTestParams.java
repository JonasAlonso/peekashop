package com.baerchen.peekashop.order.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RequestTestParams implements Serializable {
    private int transactions;
    private String type;
    private String queue;
}
