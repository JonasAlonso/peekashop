package com.baerchen.peekashop.operation.boundary;

import com.baerchen.peekashop.operation.entity.Operation;

public interface QueuePublisher {
    void publish(Operation operation);

}
