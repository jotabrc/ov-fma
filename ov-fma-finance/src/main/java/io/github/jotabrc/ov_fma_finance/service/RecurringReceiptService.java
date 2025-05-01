package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.RecurringReceiptDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface RecurringReceiptService {

    String addRecurringReceipt(@NotNull RecurringReceiptDto dto);
    void updateRecurringReceipt(@NotNull RecurringReceiptDto dto);
    void deleteRecurringReceipt(long id);
}
