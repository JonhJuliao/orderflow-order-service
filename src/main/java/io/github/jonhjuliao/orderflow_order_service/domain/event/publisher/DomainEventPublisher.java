package io.github.jonhjuliao.orderflow_order_service.domain.event.publisher;

import io.github.jonhjuliao.orderflow_order_service.domain.event.DomainEvent;

public interface DomainEventPublisher {

    void publish(DomainEvent domainEvent);
}
