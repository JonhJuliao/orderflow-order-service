package io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String topic, String key, Object payload) {
        kafkaTemplate.send(topic, key, payload);
    }
}
