package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.RecurringReceiptDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public sealed interface RecurringReceiptService permits RecurringReceiptServiceImpl {

    String save(@NotNull String userUuid, @NotNull RecurringReceiptDto dto);
    void update(@NotNull String userUuid, @NotNull RecurringReceiptDto dto);
    void delete(@NotNull String userUuid, @NotNull String uuid);
}
