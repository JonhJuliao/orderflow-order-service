package io.github.jonhjuliao.orderflow_order_service.api.controller;

import io.github.jonhjuliao.orderflow_order_service.api.dto.CreateOrderRequest;
import io.github.jonhjuliao.orderflow_order_service.api.dto.CreateOrderResponse;
import io.github.jonhjuliao.orderflow_order_service.api.dto.GetOrderResponse;
import io.github.jonhjuliao.orderflow_order_service.api.dto.OrderSummaryResponse;
import io.github.jonhjuliao.orderflow_order_service.domain.entity.Order;
import io.github.jonhjuliao.orderflow_order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public GetOrderResponse getById(@PathVariable UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        return GetOrderResponse.from(order);
    }

    @GetMapping
    public Page<OrderSummaryResponse> list(
            @RequestParam(required = false) UUID customerId,
            Pageable pageable
    ) {
        return orderService.listOrders(customerId, pageable)
                .map(OrderSummaryResponse::from);
    }

}
