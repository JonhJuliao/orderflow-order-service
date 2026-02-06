package io.github.jonhjuliao.orderflow_order_service.domain.event;

import io.github.jonhjuliao.orderflow_order_service.domain.enums.OrderStatus;

import java.util.UUID;

public class OrderStatusUpdatedEvent implements DomainEvent {

    private final UUID orderId;
    private final OrderStatus newStatus;

    public OrderStatusUpdatedEvent(UUID orderId, OrderStatus newStatus) {
        this.orderId = orderId;
        this.newStatus = newStatus;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public OrderStatus getNewStatus() {
        return newStatus;
    }
}
