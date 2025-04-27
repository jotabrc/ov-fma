package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.UserFinanceDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface FinanceService {

    void addUserFinance(@NotNull UserFinanceDto dto);

    void addReceipt();

    void addRecurringReceipt();
}
