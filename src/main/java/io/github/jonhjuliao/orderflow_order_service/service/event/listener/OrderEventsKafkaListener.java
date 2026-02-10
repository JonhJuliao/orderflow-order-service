package io.github.jonhjuliao.orderflow_order_service.service.event.listener;

import io.github.jonhjuliao.orderflow_order_service.domain.event.OrderCreatedEvent;
import io.github.jonhjuliao.orderflow_order_service.domain.event.OrderStatusUpdatedEvent;
import io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.event.OrderCreatedKafkaEvent;
import io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.event.OrderStatusUpdatedKafkaEvent;
import io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.producer.OrderKafkaProducer;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsKafkaListener {

    static final String TOPIC_ORDER_CREATED = "order.created";
    static final String TOPIC_ORDER_STATUS_UPDATED = "order.status-updated";

    private final OrderKafkaProducer producer;

    public OrderEventsKafkaListener(OrderKafkaProducer producer) {
        this.producer = producer;
    }

    @EventListener
    public void on(OrderCreatedEvent event) {
        producer.publish(
                TOPIC_ORDER_CREATED,
                event.getOrderId().toString(),
                new OrderCreatedKafkaEvent(event.getOrderId())
        );
    }

    @EventListener
    public void on(OrderStatusUpdatedEvent event) {
        producer.publish(
                TOPIC_ORDER_STATUS_UPDATED,
                event.getOrderId().toString(),
                new OrderStatusUpdatedKafkaEvent(event.getOrderId(), event.getNewStatus())
        );
    }
}
