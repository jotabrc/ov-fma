package io.github.jotabrc.ov_fma_finance.repository;

import io.github.jotabrc.ov_fma_finance.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {

    boolean existsByName(final String name);

    Optional<Party> findByName(final String name);
}
