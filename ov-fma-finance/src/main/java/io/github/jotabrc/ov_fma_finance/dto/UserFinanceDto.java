package io.github.jotabrc.ov_fma_finance.dto;

import io.github.jotabrc.ov_fma_finance.model.FinancialEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class UserFinanceDto {

    private final String userUuid;
    private final String email;
    private final String name;
    private final List<FinancialEntity> financialItems;
}
