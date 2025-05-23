package io.github.jotabrc.ov_fma_finance.repository;

import io.github.jotabrc.ov_fma_finance.model.RecurringReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecurringReceiptRepository extends JpaRepository<RecurringReceipt, Long> {

    Optional<RecurringReceipt> findByUuid(final String uuid);
}
