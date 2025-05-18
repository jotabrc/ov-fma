package io.github.jotabrc.ov_fma_finance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@Accessors(chain = true)
public class UserFinanceDto implements Serializable {

    private final String userUuid;
    private final String username;
    private final String email;
    private final String name;
    private boolean isActive;
    private final List<FinancialEntityDto> financialItems;

    @JsonCreator
    public UserFinanceDto(
            @JsonProperty("userUuid") String userUuid,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("name") String name,
            @JsonProperty("isActive") boolean isActive,
            @JsonProperty("financialItems") List<FinancialEntityDto> financialItems) {
        this.userUuid = userUuid;
        this.username = username;
        this.email = email;
        this.name = name;
        this.isActive = isActive;
        this.financialItems = financialItems;
    }
}
