package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.PaymentDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface PaymentService {

    String addPayment(@NotNull String userUuid, @NotNull PaymentDto dto);
    void updatePayment(@NotNull String userUuid, @NotNull PaymentDto dto);
    void deletePayment(@NotNull String userUuid, @NotNull String uuid);
}
