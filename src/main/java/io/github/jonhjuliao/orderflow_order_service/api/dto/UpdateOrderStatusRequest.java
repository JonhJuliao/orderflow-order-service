package io.github.jonhjuliao.orderflow_order_service.api.dto;

import io.github.jonhjuliao.orderflow_order_service.domain.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateOrderStatusRequest {

    @NotNull
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }
}

