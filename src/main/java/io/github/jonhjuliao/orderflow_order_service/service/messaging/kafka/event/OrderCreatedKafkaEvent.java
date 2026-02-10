package io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.event;

import java.util.UUID;

public class OrderCreatedKafkaEvent {

    private UUID orderId;

    public OrderCreatedKafkaEvent() {
    }

    public OrderCreatedKafkaEvent(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
