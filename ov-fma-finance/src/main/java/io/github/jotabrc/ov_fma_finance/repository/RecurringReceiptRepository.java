package io.github.jotabrc.ov_fma_finance.repository;

import io.github.jotabrc.ov_fma_finance.model.RecurringReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringReceiptRepository extends JpaRepository<RecurringReceipt, Long> {
}
