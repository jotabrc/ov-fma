package io.github.jotabrc.ov_fma_finance.repository;

import io.github.jotabrc.ov_fma_finance.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
