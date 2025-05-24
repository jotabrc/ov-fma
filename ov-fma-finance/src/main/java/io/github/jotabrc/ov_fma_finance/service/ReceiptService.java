package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.ReceiptDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ReceiptService {

    String addReceipt(@NotNull String userUuid, @NotNull ReceiptDto dto);
    void updateReceipt(@NotNull String userUuid, @NotNull ReceiptDto dto);
    void deleteReceipt(@NotNull String userUuid, @NotNull String uuid);
}
