package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.RecurringPaymentDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface RecurringPaymentService {

    String addRecurringPayment(@NotNull String userUuid, @NotNull RecurringPaymentDto dto);
    void updateRecurringPayment(@NotNull String userUuid, @NotNull RecurringPaymentDto dto);
    void deleteRecurringPayment(@NotNull String userUuid, @NotNull String uuid);
}
