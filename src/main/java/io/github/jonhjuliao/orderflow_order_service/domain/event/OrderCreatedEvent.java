package io.github.jonhjuliao.orderflow_order_service.domain.event;

import java.util.UUID;

public class OrderCreatedEvent implements DomainEvent {

    private final UUID orderId;

    public OrderCreatedEvent(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderId() {
        return orderId;
    }
}
