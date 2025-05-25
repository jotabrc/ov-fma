package io.github.jotabrc.ov_fma_finance.controller;

import io.github.jotabrc.ov_fma_finance.dto.PaymentDto;
import io.github.jotabrc.ov_fma_finance.dto.RecurringPaymentDto;
import io.github.jotabrc.ov_fma_finance.service.PaymentServiceImpl;
import io.github.jotabrc.ov_fma_finance.service.RecurringPaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentServiceImpl paymentService;

    @Mock
    private RecurringPaymentServiceImpl recurringPaymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void ini() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(paymentController)
                .build();
    }

    @Test
    void addPayment() throws Exception {
        String userUuid = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        String json = """
                {
                	"uuid": "%s",
                	"dueDate": [
                		2025,
                		5,
                		24
                	],
                	"amount": 1000,
                	"description": "Description",
                	"payee": "Payee"
                }
                """.formatted(uuid);
        when(paymentService.addPayment(any(), any())).thenReturn(uuid);
        mockMvc.perform(post("/api/v1/finance/user/%s/payment".formatted(userUuid))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains(uuid);
                    assert response.contains("Payment has been created");
                })
                .andDo(print());
    }

    @Test
    void updatePayment() throws Exception {
        String userUuid = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        PaymentDto dto = new PaymentDto(
                uuid,
                LocalDate.now(),
                100.00,
                "Test",
                "Payee"
        );
        String json = """
                {
                	"uuid": "%s",
                	"dueDate": [
                		%d,
                		%d,
                		%d
                	],
                	"amount": %.2f,
                	"description": "%s",
                	"payee": "%s"
                }
                """.formatted(dto.getUuid(),
                dto.getDueDate().getYear(),
                dto.getDueDate().getMonthValue(),
                dto.getDueDate().getDayOfMonth(),
                dto.getAmount(),
                dto.getDescription(),
                dto.getPayee());
        doNothing().when(paymentService).updatePayment(userUuid, dto);
        mockMvc.perform(put("/api/v1/finance/user/%s/payment/update".formatted(userUuid))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.equals("Payment with UUID %s has been updated".formatted(dto.getUuid()));
                })
                .andDo(print());
    }

    @Test
    void deletePayment() throws Exception {
        String userUuid = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        doNothing().when(paymentService).deletePayment(userUuid,uuid);

        PaymentDto dto = new PaymentDto(
                uuid,
                LocalDate.now(),
                100.00,
                "Test",
                "Payee"
        );
        String json = """
                {
                	"uuid": "%s",
                	"dueDate": [
                		%d,
                		%d,
                		%d
                	],
                	"amount": %.2f,
                	"description": "%s",
                	"payee": "%s"
                }
                """.formatted(dto.getUuid(),
                dto.getDueDate().getYear(),
                dto.getDueDate().getMonthValue(),
                dto.getDueDate().getDayOfMonth(),
                dto.getAmount(),
                dto.getDescription(),
                dto.getPayee());

        mockMvc.perform(delete("/api/v1/finance/user/%s/payment/%s".formatted(userUuid, uuid)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.equals("Payment with UUID %s has been deleted".formatted(uuid));
                })
                .andDo(print());
    }

    @Test
    void addRecurringPayment() throws Exception {
        String userUuid = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        String json = """
                {
                	"uuid": "%s",
                	"dueDate": [
                		2025,
                		5,
                		24
                	],
                	"amount": 1000,
                	"description": "Description",
                	"recurring_until": [
                	    2025,
                	    6,
                	    22
                	],
                	"payee": "Payee"
                }
                """.formatted(uuid);
        when(recurringPaymentService.addRecurringPayment(any(), any())).thenReturn(uuid);
        mockMvc.perform(post("/api/v1/finance/user/%s/payment/recurring".formatted(userUuid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains(uuid);
                    assert response.contains("Recurring Payment has been created");
                })
                .andDo(print());
    }

    @Test
    void updateRecurringPayment() throws Exception {
        String userUuid = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        RecurringPaymentDto dto = new RecurringPaymentDto(
                uuid,
                LocalDate.now(),
                100.00,
                "Test",
                LocalDate.now().plusMonths(1),
                "Payee"
        );
        String json = """
                {
                	"uuid": "%s",
                	"dueDate": [
                		%d,
                		%d,
                		%d
                	],
                	"amount": %.2f,
                	"description": "%s",
                	"recurring_until": [
                	    %d,
                	    %d,
                	    %d
                	],
                	"payee": "%s"
                }
                """.formatted(dto.getUuid(),
                dto.getDueDate().getYear(),
                dto.getDueDate().getMonthValue(),
                dto.getDueDate().getDayOfMonth(),
                dto.getAmount(),
                dto.getDescription(),
                dto.getRecurringUntil().getYear(),
                dto.getRecurringUntil().getDayOfMonth(),
                dto.getRecurringUntil().getDayOfMonth(),
                dto.getPayee());
        doNothing().when(recurringPaymentService).updateRecurringPayment(userUuid, dto);
        mockMvc.perform(put("/api/v1/finance/user/%s/payment/recurring/update".formatted(userUuid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.equals("Recurring Payment with UUID %s has been updated".formatted(dto.getUuid()));
                })
                .andDo(print());
    }

    @Test
    void deleteRecurringPayment() throws Exception {
        String userUuid = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        doNothing().when(paymentService).deletePayment(userUuid,uuid);
        RecurringPaymentDto dto = new RecurringPaymentDto(
                uuid,
                LocalDate.now(),
                100.00,
                "Test",
                LocalDate.now().plusMonths(1),
                "Payee"
        );
        String json = """
                {
                	"uuid": "%s",
                	"dueDate": [
                		%d,
                		%d,
                		%d
                	],
                	"amount": %.2f,
                	"description": "%s",
                	"recurring_until": [
                	    %d,
                	    %d,
                	    %d
                	],
                	"payee": "%s"
                }
                """.formatted(dto.getUuid(),
                dto.getDueDate().getYear(),
                dto.getDueDate().getMonthValue(),
                dto.getDueDate().getDayOfMonth(),
                dto.getAmount(),
                dto.getDescription(),
                dto.getRecurringUntil().getYear(),
                dto.getRecurringUntil().getDayOfMonth(),
                dto.getRecurringUntil().getDayOfMonth(),
                dto.getPayee());

        mockMvc.perform(delete("/api/v1/finance/user/%s/payment/recurring/%s".formatted(userUuid, uuid)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.equals("Recurring Payment with UUID %s has been deleted".formatted(uuid));
                })
                .andDo(print());
    }
}