package io.github.jotabrc.ov_fma_finance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@Entity(name = "tb_recurrence")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Recurrence extends FinancialEntity {

    @Column(nullable = false, name = "recurrence_day")
    private int day;

    @Column(nullable = false, name = "recurrence_month")
    private int month;

    @Column(nullable = false, name = "recurrence_year")
    private int year;

    public Recurrence(long id, String uuid, UserFinance userFinance, BigDecimal amount, String description, LocalDateTime createdAt,
                      LocalDateTime updatedAt, long version, int day, int month, int year) {
        super(id, uuid, userFinance, amount, description, createdAt, updatedAt, version);
        this.day = day;
        this.month = month;
        this.year = year;
    }
}
