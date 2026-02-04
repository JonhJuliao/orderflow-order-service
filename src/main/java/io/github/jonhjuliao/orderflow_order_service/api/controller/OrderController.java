package io.github.jonhjuliao.orderflow_order_service.api.controller;

import io.github.jonhjuliao.orderflow_order_service.api.dto.CreateOrderRequest;
import io.github.jonhjuliao.orderflow_order_service.api.dto.CreateOrderResponse;
import io.github.jonhjuliao.orderflow_order_service.domain.entity.Order;
import io.github.jonhjuliao.orderflow_order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse create(@RequestBody @Valid CreateOrderRequest request) {
        Order order = orderService.createOrder(request.getCustomerId(), request.getTotal());
        return new CreateOrderResponse(order.getId(), order.getStatus(), order.getTotal());
    }
}
