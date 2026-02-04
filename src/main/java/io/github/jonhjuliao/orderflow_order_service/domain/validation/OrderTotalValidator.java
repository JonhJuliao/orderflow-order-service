package io.github.jonhjuliao.orderflow_order_service.domain.validation;

import io.github.jonhjuliao.orderflow_order_service.domain.entity.Order;
import io.github.jonhjuliao.orderflow_order_service.domain.exception.BusinessRuleException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderTotalValidator implements OrderValidator {

    @Override
    public void validate(Order order) {
        if (order.getTotal() == null || order.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Order total must be greater than zero.");
        }
    }
}
