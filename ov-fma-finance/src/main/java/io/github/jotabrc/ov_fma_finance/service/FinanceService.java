package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.UserFinanceDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface FinanceService {

    UserFinanceDto addUserFinance(@NotNull UserFinanceDto dto);
    UserFinanceDto updateUserFinance(@NotNull UserFinanceDto dto);
}
