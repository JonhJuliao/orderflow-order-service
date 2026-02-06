package io.github.jonhjuliao.orderflow_order_service.service;

import io.github.jonhjuliao.orderflow_order_service.domain.entity.Order;
import io.github.jonhjuliao.orderflow_order_service.domain.exception.OrderNotFoundException;
import io.github.jonhjuliao.orderflow_order_service.domain.repository.OrderRepository;
import io.github.jonhjuliao.orderflow_order_service.domain.validation.OrderValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final List<OrderValidator> validators;

    public OrderService(OrderRepository orderRepository, List<OrderValidator> validators) {
        this.orderRepository = orderRepository;
        this.validators = validators;
    }

    public Order createOrder(UUID customerId, BigDecimal total) {
        Order order = new Order(customerId, total);

        validators.forEach(v -> v.validate(order));

        return orderRepository.save(order);
    }

    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

}
