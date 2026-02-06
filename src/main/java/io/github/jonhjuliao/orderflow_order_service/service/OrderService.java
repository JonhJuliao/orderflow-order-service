package io.github.jonhjuliao.orderflow_order_service.service;

import io.github.jonhjuliao.orderflow_order_service.domain.entity.Order;
import io.github.jonhjuliao.orderflow_order_service.domain.enums.OrderStatus;
import io.github.jonhjuliao.orderflow_order_service.domain.event.OrderCreatedEvent;
import io.github.jonhjuliao.orderflow_order_service.domain.event.OrderStatusUpdatedEvent;
import io.github.jonhjuliao.orderflow_order_service.domain.event.publisher.DomainEventPublisher;
import io.github.jonhjuliao.orderflow_order_service.domain.exception.BusinessRuleException;
import io.github.jonhjuliao.orderflow_order_service.domain.exception.OrderNotFoundException;
import io.github.jonhjuliao.orderflow_order_service.domain.repository.OrderRepository;
import io.github.jonhjuliao.orderflow_order_service.domain.validation.OrderValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final List<OrderValidator> validators;

    private final DomainEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, List<OrderValidator> validators, DomainEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.validators = validators;
        this.eventPublisher = eventPublisher;
    }

    public Order createOrder(UUID customerId, BigDecimal total) {
        Order order = new Order(customerId, total);

        validators.forEach(v -> v.validate(order));

        Order saved = orderRepository.save(order);
        eventPublisher.publish(new OrderCreatedEvent(saved.getId()));
        return saved;
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public Page<Order> listOrders(UUID customerId, Pageable pageable) {
        if (customerId != null) {
            return orderRepository.findAllByCustomerId(customerId, pageable);
        }
        return orderRepository.findAll(pageable);
    }

    @Transactional
    public Order updateStatus(UUID orderId, OrderStatus newStatus) {
        Order order = getOrderById(orderId);

        if (order.getStatus() == OrderStatus.CANCELLED ||
                order.getStatus() == OrderStatus.COMPLETED) {
            throw new BusinessRuleException("Order cannot change status anymore");
        }

        if (newStatus == OrderStatus.CREATED) {
            throw new BusinessRuleException("Invalid status transition");
        }

        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);
        eventPublisher.publish(new OrderStatusUpdatedEvent(saved.getId(), newStatus));
        return saved;
    }


}
