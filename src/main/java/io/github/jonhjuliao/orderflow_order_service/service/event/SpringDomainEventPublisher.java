package io.github.jonhjuliao.orderflow_order_service.service.event;

import io.github.jonhjuliao.orderflow_order_service.domain.event.DomainEvent;
import io.github.jonhjuliao.orderflow_order_service.domain.event.publisher.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(DomainEvent event) {
        publisher.publishEvent(event);
    }
}
