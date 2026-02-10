package io.github.jonhjuliao.orderflow_order_service.service.event.listener;

import io.github.jonhjuliao.orderflow_order_service.domain.event.OrderCreatedEvent;
import io.github.jonhjuliao.orderflow_order_service.domain.event.OrderStatusUpdatedEvent;
import io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.config.OrderflowKafkaTopicsProperties;
import io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.event.OrderCreatedKafkaEvent;
import io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.event.OrderStatusUpdatedKafkaEvent;
import io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.producer.OrderKafkaProducer;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsKafkaListener {

    private final OrderKafkaProducer producer;
    private final OrderflowKafkaTopicsProperties topics;

    public OrderEventsKafkaListener(OrderKafkaProducer producer, OrderflowKafkaTopicsProperties topics) {
        this.producer = producer;
        this.topics = topics;
    }

    @EventListener
    public void on(OrderCreatedEvent event) {
        producer.publish(
                topics.getOrderCreated(),
                event.getOrderId().toString(),
                new OrderCreatedKafkaEvent(event.getOrderId())
        );
    }

    @EventListener
    public void on(OrderStatusUpdatedEvent event) {
        producer.publish(
                topics.getOrderStatusUpdated(),
                event.getOrderId().toString(),
                new OrderStatusUpdatedKafkaEvent(event.getOrderId(), event.getNewStatus())
        );
    }
}
