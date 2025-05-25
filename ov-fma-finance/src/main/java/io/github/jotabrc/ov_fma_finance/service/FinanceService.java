package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.UserFinanceDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
public sealed interface FinanceService permits FinanceServiceImpl {

    void save(@NotNull UserFinanceDto dto);
    void update(@NotNull UserFinanceDto dto);
    Page<UserFinanceDto> get(@NotNull String userUuid,
                             @NotNull LocalDate fromDate,
                             @NotNull LocalDate toDate,
                             int pageStart,
                             int pageSize);
}
