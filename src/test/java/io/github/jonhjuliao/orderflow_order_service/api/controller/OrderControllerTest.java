package io.github.jonhjuliao.orderflow_order_service.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.jonhjuliao.orderflow_order_service.domain.entity.Order;
import io.github.jonhjuliao.orderflow_order_service.domain.exception.BusinessRuleException;
import io.github.jonhjuliao.orderflow_order_service.domain.exception.OrderNotFoundException;
import io.github.jonhjuliao.orderflow_order_service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    void shouldReturn201WhenRequestIsValid() throws Exception {
        UUID customerId = UUID.randomUUID();
        BigDecimal total = new BigDecimal("100.00");

        Order created = new Order(customerId, total);

        when(orderService.createOrder(any(UUID.class), any(BigDecimal.class)))
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

    @Test
    void shouldReturn400WhenBusinessRuleExceptionOccurs() throws Exception {
        UUID customerId = UUID.randomUUID();

        when(orderService.createOrder(any(UUID.class), any(BigDecimal.class)))
                .thenThrow(new BusinessRuleException("Order total must be greater than zero."));

        String body = """
            {
              "customerId": "%s",
              "total": 10.00
            }
            """.formatted(customerId);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Order total must be greater than zero."))
                .andExpect(jsonPath("$.path").value("/orders"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturn400WithFieldErrorsWhenRequestIsInvalid() throws Exception {
        String body = """
            {
              "total": 100.00
            }
            """;

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.customerId").exists())
                .andExpect(jsonPath("$.path").value("/orders"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void deveRetornar200QuandoPedidoExistir() throws Exception {
        UUID orderId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), BigDecimal.valueOf(15.50));

        when(orderService.getOrderById(orderId)).thenReturn(order);

        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(order.getCustomerId().toString()))
                .andExpect(jsonPath("$.status").value(order.getStatus().name()))
                .andExpect(jsonPath("$.total").value(15.50));
    }

    @Test
    void deveRetornar404QuandoPedidoNaoExistir() throws Exception {
        UUID orderId = UUID.randomUUID();

        when(orderService.getOrderById(orderId)).thenThrow(new OrderNotFoundException(orderId));

        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().isNotFound())
                // valida seu formato padronizado (ApiErrorResponse)
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.path").value("/orders/" + orderId));
    }

    @Test
    void deveRetornar400QuandoUuidForInvalido() throws Exception {
        mockMvc.perform(get("/orders/{orderId}", "uuid-invalido"))
                .andExpect(status().isBadRequest())
                // valida seu formato padronizado (ApiErrorResponse)
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/orders/uuid-invalido"));
    }

    @Test
    void deveRetornar400QuandoCustomerIdInvalido() throws Exception {
        mockMvc.perform(get("/orders")
                        .param("customerId", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

}

