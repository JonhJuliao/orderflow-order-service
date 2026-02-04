package io.github.jonhjuliao.orderflow_order_service.service;


import io.github.jonhjuliao.orderflow_order_service.domain.entity.Order;
import io.github.jonhjuliao.orderflow_order_service.domain.exception.BusinessRuleException;
import io.github.jonhjuliao.orderflow_order_service.domain.repository.OrderRepository;
import io.github.jonhjuliao.orderflow_order_service.domain.validation.OrderValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private OrderValidator orderValidator;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderValidator = mock(OrderValidator.class);

        orderService = new OrderService(orderRepository, List.of(orderValidator));
    }

    @Test
    void shouldCreateOrderWhenDataIsValid() {
        UUID customerId = UUID.randomUUID();
        BigDecimal total = new BigDecimal("100.00");

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.createOrder(customerId, total);

        assertNotNull(order);
        assertEquals(customerId, order.getCustomerId());
        assertEquals(total, order.getTotal());

        verify(orderValidator).validate(any(Order.class));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowExceptionWhenValidatorFails() {
        UUID customerId = UUID.randomUUID();
        BigDecimal total = new BigDecimal("-10.00");

        doThrow(new BusinessRuleException("Invalid order"))
                .when(orderValidator).validate(any(Order.class));

        assertThrows(
                BusinessRuleException.class,
                () -> orderService.createOrder(customerId, total)
        );

        verify(orderRepository, never()).save(any());
    }
}

