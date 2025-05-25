package io.github.jotabrc.ov_fma_finance.controller;

import io.github.jotabrc.ov_fma_finance.dto.UserFinanceDto;
import io.github.jotabrc.ov_fma_finance.model.Receipt;
import io.github.jotabrc.ov_fma_finance.model.RecurringPayment;
import io.github.jotabrc.ov_fma_finance.model.RecurringReceipt;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.service.FinanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FinanceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FinanceServiceImpl financeService;

    @InjectMocks
    private FinanceController financeController;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(financeController)
                .build();
    }

    @Test
    void getTest() throws Exception {
        UserFinance userFinance = UserFinance
                .builder()
                .id(1)
                .userUuid("2f0d0980-8846-4e46-a7db-2a6d1cbd0e9d")
                .name("John Doe")
                .financialItems(new ArrayList<>())
                .build();
        RecurringReceipt recurringReceipt = new RecurringReceipt(
                1,
                "519a3cc5-5f96-484e-9eee-3077bf3108ff",
                userFinance,
                LocalDate.of(2025, 5, 24),
                1000.00,
                "Description",
                LocalDateTime.now(),
                null,
                0,
                LocalDate.now(),
                "Vendor"
        );
        RecurringPayment recurringPayment = new RecurringPayment(
                1,
                "b94bdf81-3260-4b33-9773-ea606290cd1d",
                userFinance,
                LocalDate.of(2025, 5, 23),
                120.23,
                "Description",
                LocalDateTime.now(),
                null,
                0,
                LocalDate.of(2025,7, 22),
                "Payee"
        );
        Receipt receipt = new Receipt(
                1,
                "0f9a3252-ed4b-4b45-b8d4-bc0ec49eed5f",
                userFinance,
                LocalDate.of(2025, 5, 24),
                123.99,
                "Description",
                LocalDateTime.now(),
                null,
                0,
                "Vendor"
        );
        userFinance.getFinancialItems().addAll(
                List.of(
                        recurringReceipt,
                        recurringPayment,
                        receipt
                )
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserFinanceDto> page = new PageImpl<>(List.of(userFinance.transform()), pageable, 0);
        when(financeService.get(
                "userUuid",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 22),
                0,
                10)
        )
                .thenReturn(page);
        mockMvc.perform(get("/api/v1/finance/user/get")
                        .param("userUuid", "userUuid")
                        .param("fromDate", "2025-01-01")
                        .param("toDate", "2025-12-22")
                        .param("pageStart", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    assert response.contains("totalElements");
                    assert response.contains(recurringReceipt.getUuid());
                    assert response.contains(userFinance.getUserUuid());
                    assert response.contains(receipt.getUuid());
                    assert response.contains(recurringPayment.getUuid());
                })
                .andDo(print());
    }
}