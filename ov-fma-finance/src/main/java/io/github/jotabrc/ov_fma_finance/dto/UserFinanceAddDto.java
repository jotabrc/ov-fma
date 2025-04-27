package io.github.jotabrc.ov_fma_finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class UserFinanceAddDto {

    private final String userUuid;
    private final String username;
    private final String email;
    private final String name;
    private boolean isActive;
}
