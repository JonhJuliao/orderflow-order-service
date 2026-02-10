package io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.event;

import io.github.jonhjuliao.orderflow_order_service.domain.enums.OrderStatus;

import java.util.UUID;

public class OrderStatusUpdatedKafkaEvent {

    private UUID orderId;
    private OrderStatus newStatus;

    public OrderStatusUpdatedKafkaEvent() {
    }

    public OrderStatusUpdatedKafkaEvent(UUID orderId, OrderStatus newStatus) {
        this.orderId = orderId;
        this.newStatus = newStatus;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(OrderStatus newStatus) {
        this.newStatus = newStatus;
    }
}
