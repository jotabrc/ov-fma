package io.github.jotabrc.ov_fma_finance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserFinanceAddDto {

    @JsonProperty("uuid")
    private String userUuid;
    private String username;
    private String email;
    private String name;
    private boolean isActive;
}
