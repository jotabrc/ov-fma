package io.github.jotabrc.ov_fma_finance.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "tb_payment")
public class Payment extends FinancialEntity {

    private String vendor;

    public Payment(long id, String uuid, UserFinance userFinance, BigDecimal amount, String description, LocalDateTime createdAt,
                   LocalDateTime updatedAt, long version, String vendor) {
        super(id, uuid, userFinance, amount, description, createdAt, updatedAt, version);
        this.vendor = vendor;
    }
}
