package io.github.jonhjuliao.orderflow_order_service.api.dto;

import io.github.jonhjuliao.orderflow_order_service.domain.entity.Order;
import io.github.jonhjuliao.orderflow_order_service.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class GetOrderResponse {

    private UUID id;
    private UUID customerId;
    private OrderStatus status;
    private BigDecimal total;
    private LocalDateTime createdAt;

    public GetOrderResponse(UUID id, UUID customerId, OrderStatus status, BigDecimal total, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
    }

    public static GetOrderResponse from(Order order) {
        return new GetOrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotal(),
                order.getCreatedAt()
        );
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
