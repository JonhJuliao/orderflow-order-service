package io.github.jonhjuliao.orderflow_order_service.domain.repository;

import io.github.jonhjuliao.orderflow_order_service.domain.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findAllByCustomerId(UUID customerId, Pageable pageable);
}
