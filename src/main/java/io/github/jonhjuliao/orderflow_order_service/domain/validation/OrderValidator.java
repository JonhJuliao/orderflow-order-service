package io.github.jonhjuliao.orderflow_order_service.domain.validation;

import io.github.jonhjuliao.orderflow_order_service.domain.entity.Order;

public interface OrderValidator {

    void validate(Order order);

}
