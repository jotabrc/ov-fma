package io.github.jotabrc.ov_fma_finance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "tb_payment")
public class Payment extends FinancialEntity {

    @Column(nullable = false, unique = false)
    private String payee;

    public Payment(long id, String uuid, UserFinance userFinance, LocalDate dueDate, double amount, String description, LocalDateTime createdAt,
                   LocalDateTime updatedAt, long version, String payee) {
        super(id, uuid, userFinance, dueDate, amount, description, createdAt, updatedAt, version);
        this.payee = payee;
    }
}
