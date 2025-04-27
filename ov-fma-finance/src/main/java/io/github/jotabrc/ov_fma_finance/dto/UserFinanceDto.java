package io.github.jotabrc.ov_fma_finance.dto;

import io.github.jotabrc.ov_fma_finance.model.FinancialEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserFinanceDto {

    private String userUuid;
    private List<FinancialEntity> financialItems;
}
