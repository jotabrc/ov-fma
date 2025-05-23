package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.UserFinanceDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public interface FinanceService {

    void addUserFinance(@NotNull UserFinanceDto dto);

    void updateUserFinance(@NotNull UserFinanceDto dto);

    Page<UserFinanceDto> get(@NotNull String userUuid,
                             @NotNull LocalDate fromDate,
                             @NotNull LocalDate toDate,
                             int pageStart,
                             int pageSize);
}
