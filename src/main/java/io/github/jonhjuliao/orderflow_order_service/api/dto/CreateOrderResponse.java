package io.github.jonhjuliao.orderflow_order_service.api.dto;

import io.github.jonhjuliao.orderflow_order_service.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateOrderResponse {

    private UUID id;
    private OrderStatus status;
    private BigDecimal total;

    public CreateOrderResponse(UUID id, OrderStatus status, BigDecimal total) {
        this.id = id;
        this.status = status;
        this.total = total;
    }

    public UUID getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotal() {
        return total;
    }
}

