package io.github.jonhjuliao.orderflow_order_service.service;


import io.github.jonhjuliao.orderflow_order_service.domain.entity.Order;
import io.github.jonhjuliao.orderflow_order_service.domain.enums.OrderStatus;
import io.github.jonhjuliao.orderflow_order_service.domain.event.publisher.DomainEventPublisher;
import io.github.jonhjuliao.orderflow_order_service.domain.exception.BusinessRuleException;
import io.github.jonhjuliao.orderflow_order_service.domain.exception.OrderNotFoundException;
import io.github.jonhjuliao.orderflow_order_service.domain.repository.OrderRepository;
import io.github.jonhjuliao.orderflow_order_service.domain.validation.OrderValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private OrderValidator orderValidator;
    private OrderService orderService;
    private DomainEventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        orderValidator = mock(OrderValidator.class);
        eventPublisher = mock(DomainEventPublisher.class);

        orderService = new OrderService(orderRepository, List.of(orderValidator), eventPublisher);
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

    @Test
    void deveRetornarPedidoQuandoExistir() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), BigDecimal.valueOf(10.00));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(order.getCustomerId(), result.getCustomerId());
        assertEquals(order.getStatus(), result.getStatus());
        assertEquals(order.getTotal(), result.getTotal());

        verify(orderRepository).findById(orderId);
    }

    @Test
    void deveLancarExcecaoQuandoPedidoNaoExistir() {
        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(orderId));

        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void deveListarPedidosSemFiltro() {
        Pageable pageable = PageRequest.of(0, 10);

        Order order = new Order(UUID.randomUUID(), BigDecimal.valueOf(50));
        Page<Order> page = new PageImpl<>(List.of(order));

        when(orderRepository.findAll(pageable)).thenReturn(page);

        Page<Order> result = orderService.listOrders(null, pageable);

        assertEquals(1, result.getContent().size());
        verify(orderRepository).findAll(pageable);
    }

    @Test
    void deveListarPedidosPorCustomerId() {
        UUID customerId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        Order order = new Order(customerId, BigDecimal.valueOf(30));
        Page<Order> page = new PageImpl<>(List.of(order));

        when(orderRepository.findAllByCustomerId(customerId, pageable)).thenReturn(page);

        Page<Order> result = orderService.listOrders(customerId, pageable);

        assertEquals(1, result.getContent().size());
        verify(orderRepository).findAllByCustomerId(customerId, pageable);
    }

    @Test
    void deveRespeitarPaginacao() {
        Pageable pageable = PageRequest.of(1, 2);

        Page<Order> page = new PageImpl<>(List.of(), pageable, 5);

        when(orderRepository.findAll(pageable)).thenReturn(page);

        Page<Order> result = orderService.listOrders(null, pageable);

        assertEquals(1, result.getNumber());
        assertEquals(2, result.getSize());
        verify(orderRepository).findAll(pageable);
    }

    @Test
    void deveAtualizarStatusParaCompletedQuandoPedidoEstiverCreated() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), BigDecimal.valueOf(100));
        // por padrão o seu Order inicia CREATED (confirmado na entidade)
        assertEquals(OrderStatus.CREATED, order.getStatus());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order updated = orderService.updateStatus(orderId, OrderStatus.COMPLETED);

        assertEquals(OrderStatus.COMPLETED, updated.getStatus());

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(captor.capture());
        assertEquals(OrderStatus.COMPLETED, captor.getValue().getStatus());
    }

    @Test
    void deveAtualizarStatusParaCancelledQuandoPedidoEstiverCreated() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), BigDecimal.valueOf(100));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order updated = orderService.updateStatus(orderId, OrderStatus.CANCELLED);

        assertEquals(OrderStatus.CANCELLED, updated.getStatus());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void deveLancarBusinessRuleQuandoPedidoJaEstiverCompleted() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), BigDecimal.valueOf(100));
        order.setStatus(OrderStatus.COMPLETED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(BusinessRuleException.class,
                () -> orderService.updateStatus(orderId, OrderStatus.CANCELLED));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void deveLancarBusinessRuleQuandoPedidoJaEstiverCancelled() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), BigDecimal.valueOf(100));
        order.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(BusinessRuleException.class,
                () -> orderService.updateStatus(orderId, OrderStatus.COMPLETED));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void deveRetornar404QuandoPedidoNaoExistir() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                () -> orderService.updateStatus(orderId, OrderStatus.COMPLETED));

        verify(orderRepository, never()).save(any());
    }
}

