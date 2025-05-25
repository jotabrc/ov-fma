package io.github.jotabrc.ov_fma_finance.repository;

import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface FinanceRepository extends JpaRepository<UserFinance, Long> {

    boolean existsByUserUuid(final String userUuid);

    Optional<UserFinance> findByUserUuid(final String userUuid);

    @Query("SELECT DISTINCT e FROM UserFinance e " +
            "JOIN e.financialItems f " +
            "LEFT JOIN Recurrence r ON r.id = f.id " +
            "WHERE f.dueDate BETWEEN :fromDate AND :toDate " +
            "AND (r.recurringUntil BETWEEN :fromDate AND :toDate OR r IS NULL) " +
            "AND e.userUuid = :uuid")
    Page<UserFinance> findByDueDate(@Param("uuid") final String uuid,
                                    @Param("fromDate") final LocalDate fromDate,
                                    @Param("toDate") final LocalDate toDate,
                                    final Pageable pageable);
}
