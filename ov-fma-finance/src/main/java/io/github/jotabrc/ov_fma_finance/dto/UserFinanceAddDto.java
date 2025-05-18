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

    /* *
    * Producer is sending JSON with field uuid for the user, however,
    * the tb_user_finance uses user_uuid for better clarity.
    * for proper Jackson parsing, @JsonProperty is being used to map,
    * the field correctly.
    * */
    @JsonProperty("uuid")
    private String userUuid;
    private String username;
    private String email;
    private String name;
    private boolean isActive;
}
