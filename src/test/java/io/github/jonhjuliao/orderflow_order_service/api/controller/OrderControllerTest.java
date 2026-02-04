package io.github.jonhjuliao.orderflow_order_service.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jonhjuliao.orderflow_order_service.domain.entity.Order;
import io.github.jonhjuliao.orderflow_order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void shouldReturn201WhenRequestIsValid() throws Exception {
        UUID customerId = UUID.randomUUID();
        BigDecimal total = new BigDecimal("100.00");

        Order created = new Order(customerId, total);

        Mockito.when(orderService.createOrder(any(UUID.class), any(BigDecimal.class)))
                .thenReturn(created);

        String body = """
                {
                  "customerId": "%s",
                  "total": 100.00
                }
                """.formatted(customerId);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.total").value(100.00));
    }

    @Test
    void shouldReturn400WhenCustomerIdIsMissing() throws Exception {
        String body = """
                {
                  "total": 100.00
                }
                """;

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}

