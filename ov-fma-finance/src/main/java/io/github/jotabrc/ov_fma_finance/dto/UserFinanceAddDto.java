package io.github.jotabrc.ov_fma_finance.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public final class UserFinanceAddDto {

    private String userUuid;
    private String name;
}
