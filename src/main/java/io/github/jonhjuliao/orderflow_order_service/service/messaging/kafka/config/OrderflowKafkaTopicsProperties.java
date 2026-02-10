package io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "orderflow.messaging.kafka.topics")
public class OrderflowKafkaTopicsProperties {

    private String orderCreated;
    private String orderStatusUpdated;

    public String getOrderCreated() {
        return orderCreated;
    }

    public void setOrderCreated(String orderCreated) {
        this.orderCreated = orderCreated;
    }

    public String getOrderStatusUpdated() {
        return orderStatusUpdated;
    }

    public void setOrderStatusUpdated(String orderStatusUpdated) {
        this.orderStatusUpdated = orderStatusUpdated;
    }
}
