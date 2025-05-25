package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.ReceiptDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public sealed interface ReceiptService permits ReceiptServiceImpl {

    String save(@NotNull String userUuid, @NotNull ReceiptDto dto);
    void update(@NotNull String userUuid, @NotNull ReceiptDto dto);
    void delete(@NotNull String userUuid, @NotNull String uuid);
}
