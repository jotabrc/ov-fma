package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.PaymentDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public sealed interface PaymentService permits PaymentServiceImpl {

    String save(@NotNull String userUuid, @NotNull PaymentDto dto);
    void update(@NotNull String userUuid, @NotNull PaymentDto dto);
    void delete(@NotNull String userUuid, @NotNull String uuid);
}
