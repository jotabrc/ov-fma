package io.github.jotabrc.ov_fma_finance.controller;

import io.github.jotabrc.ov_fma_finance.dto.ReceiptDto;
import io.github.jotabrc.ov_fma_finance.dto.RecurringReceiptDto;
import io.github.jotabrc.ov_fma_finance.service.ReceiptServiceImpl;
import io.github.jotabrc.ov_fma_finance.service.RecurringReceiptServiceImpl;
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

class ReceiptControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReceiptServiceImpl receiptService;

    @Mock
    private RecurringReceiptServiceImpl recurringReceiptService;

    @InjectMocks
    private ReceiptController receiptController;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(receiptController)
                .build();
    }

    @Test
    void addReceipt() throws Exception {
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
                	"vendor": "vendor"
                }
                """.formatted(uuid);
        when(receiptService.addReceipt(any(), any())).thenReturn(uuid);
        mockMvc.perform(post("/api/v1/finance/user/%s/receipt".formatted(userUuid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains(uuid);
                    assert response.contains("Receipt has been created");
                })
                .andDo(print());
    }

    @Test
    void updateReceipt() throws Exception {
        String userUuid = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        ReceiptDto dto = new ReceiptDto(
                uuid,
                LocalDate.now(),
                100.00,
                "Test",
                "Vendor"
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
                	"vendor": "%s"
                }
                """.formatted(dto.getUuid(),
                dto.getDueDate().getYear(),
                dto.getDueDate().getMonthValue(),
                dto.getDueDate().getDayOfMonth(),
                dto.getAmount(),
                dto.getDescription(),
                dto.getVendor());
        doNothing().when(receiptService).updateReceipt(userUuid, dto);
        mockMvc.perform(put("/api/v1/finance/user/%s/receipt/update".formatted(userUuid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.equals("Receipt with UUID %s has been updated".formatted(dto.getUuid()));
                })
                .andDo(print());
    }

    @Test
    void deleteReceipt() throws Exception {
        String userUuid = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        doNothing().when(receiptService).deleteReceipt(userUuid,uuid);

        ReceiptDto dto = new ReceiptDto(
                uuid,
                LocalDate.now(),
                100.00,
                "Test",
                "Vendor"
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
                dto.getVendor());

        mockMvc.perform(delete("/api/v1/finance/user/%s/receipt/%s".formatted(userUuid, uuid)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.equals("Receipt with UUID %s has been deleted".formatted(uuid));
                })
                .andDo(print());
    }

    @Test
    void addRecurringReceipt() throws Exception {
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
                	"vendor": "Vendor"
                }
                """.formatted(uuid);
        when(recurringReceiptService.addRecurringReceipt(any(), any())).thenReturn(uuid);
        mockMvc.perform(post("/api/v1/finance/user/%s/receipt/recurring".formatted(userUuid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains(uuid);
                    assert response.contains("Recurring Receipt has been created");
                })
                .andDo(print());
    }

    @Test
    void updateRecurringReceipt() throws Exception {
        String userUuid = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        RecurringReceiptDto dto = new RecurringReceiptDto(
                uuid,
                LocalDate.now(),
                100.00,
                "Test",
                LocalDate.now().plusMonths(1),
                "Vendor"
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
                	"vendor": "%s"
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
                dto.getVendor());
        doNothing().when(recurringReceiptService).updateRecurringReceipt(userUuid, dto);
        mockMvc.perform(put("/api/v1/finance/user/%s/receipt/recurring/update".formatted(userUuid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.equals("Recurring Receipt with UUID %s has been updated".formatted(dto.getUuid()));
                })
                .andDo(print());
    }

    @Test
    void deleteRecurringReceipt() throws Exception {
        String userUuid = UUID.randomUUID().toString();
        String uuid = UUID.randomUUID().toString();
        doNothing().when(recurringReceiptService).deleteRecurringReceipt(userUuid,uuid);
        RecurringReceiptDto dto = new RecurringReceiptDto(
                uuid,
                LocalDate.now(),
                100.00,
                "Test",
                LocalDate.now().plusMonths(1),
                "Vendor"
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
                	"vendor": "%s"
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
                dto.getVendor());

        mockMvc.perform(delete("/api/v1/finance/user/%s/receipt/recurring/%s".formatted(userUuid, uuid)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.equals("Recurring Receipt with UUID %s has been deleted".formatted(uuid));
                })
                .andDo(print());
    }
}