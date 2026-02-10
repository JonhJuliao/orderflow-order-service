package io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private Logger log = LoggerFactory.getLogger(OrderKafkaProducer.class);

    public OrderKafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String topic, String key, Object payload) {
        kafkaTemplate.send(topic, key, payload)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Falha ao publicar evento no Kafka. topic={}, key={}", topic, key, ex);
                    } else {
                        log.debug("Evento publicado no Kafka. topic={}, key={}", topic, key);
                    }
                });
    }
}
