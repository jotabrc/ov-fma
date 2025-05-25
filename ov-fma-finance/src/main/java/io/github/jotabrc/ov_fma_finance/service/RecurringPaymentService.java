package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.RecurringPaymentDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public sealed interface RecurringPaymentService permits RecurringPaymentServiceImpl {

    String save(@NotNull String userUuid, @NotNull RecurringPaymentDto dto);
    void update(@NotNull String userUuid, @NotNull RecurringPaymentDto dto);
    void delete(@NotNull String userUuid, @NotNull String uuid);
}
