package io.github.jotabrc.ov_fma_finance.repository;

import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinanceRepository extends JpaRepository<UserFinance, Long> {

    boolean existsByUserUuid(final String userUuid);

    Optional<UserFinance> findByUserUuid(final String userUuid);
}
