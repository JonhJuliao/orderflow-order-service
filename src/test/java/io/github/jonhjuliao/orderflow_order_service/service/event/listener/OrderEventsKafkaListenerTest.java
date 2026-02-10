package io.github.jonhjuliao.orderflow_order_service.service.event.listener;

import io.github.jonhjuliao.orderflow_order_service.domain.event.OrderCreatedEvent;
import io.github.jonhjuliao.orderflow_order_service.domain.event.OrderStatusUpdatedEvent;
import io.github.jonhjuliao.orderflow_order_service.domain.enums.OrderStatus;
import io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.config.OrderflowKafkaTopicsProperties;
import io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.event.OrderCreatedKafkaEvent;
import io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.event.OrderStatusUpdatedKafkaEvent;
import io.github.jonhjuliao.orderflow_order_service.service.messaging.kafka.producer.OrderKafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderEventsKafkaListenerTest {

    @Mock
    private OrderKafkaProducer producer;

    @Mock
    private OrderflowKafkaTopicsProperties topics;

    private OrderEventsKafkaListener listener;

    @BeforeEach
    void setUp() {
        listener = new OrderEventsKafkaListener(producer, topics);
    }

    @Test
    void devePublicarEventoDePedidoCriadoNoTopicoConfigurado() {
        String topic = "order.created";
        UUID orderId = UUID.randomUUID();

        when(topics.getOrderCreated()).thenReturn(topic);

        listener.on(new OrderCreatedEvent(orderId));

        ArgumentCaptor<Object> payloadCaptor = ArgumentCaptor.forClass(Object.class);

        verify(producer, times(1)).publish(eq(topic), eq(orderId.toString()), payloadCaptor.capture());
        verifyNoMoreInteractions(producer);

        Object payload = payloadCaptor.getValue();
        // Valida tipo e conteúdo do payload
        OrderCreatedKafkaEvent event = (OrderCreatedKafkaEvent) payload;
        assertEquals(orderId, event.getOrderId());
    }

    @Test
    void devePublicarEventoDeAtualizacaoDeStatusNoTopicoConfigurado() {
        String topic = "order.status-updated";
        UUID orderId = UUID.randomUUID();
        OrderStatus newStatus = OrderStatus.COMPLETED;

        when(topics.getOrderStatusUpdated()).thenReturn(topic);

        listener.on(new OrderStatusUpdatedEvent(orderId, newStatus));

        ArgumentCaptor<Object> payloadCaptor = ArgumentCaptor.forClass(Object.class);

        verify(producer, times(1)).publish(eq(topic), eq(orderId.toString()), payloadCaptor.capture());
        verifyNoMoreInteractions(producer);

        Object payload = payloadCaptor.getValue();
        // Valida tipo e conteúdo do payload
        OrderStatusUpdatedKafkaEvent event = (OrderStatusUpdatedKafkaEvent) payload;
        assertEquals(orderId, event.getOrderId());
        assertEquals(newStatus, event.getNewStatus());
    }
}
