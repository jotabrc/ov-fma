package io.github.jotabrc.ov_fma_finance.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "tb_receipt")
public class Receipt extends FinancialEntity {

    private String payee;

    public Receipt(long id, String uuid, UserFinance userFinance, BigDecimal amount, String description, LocalDateTime createdAt,
                   LocalDateTime updatedAt, long version, String payee) {
        super(id, uuid, userFinance, amount, description, createdAt, updatedAt, version);
        this.payee = payee;
    }
}
