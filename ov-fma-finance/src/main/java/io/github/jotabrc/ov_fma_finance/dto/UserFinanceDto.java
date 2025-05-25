package io.github.jotabrc.ov_fma_finance.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@Accessors(chain = true)
public final class UserFinanceDto implements Serializable {

    private final String userUuid;
    private final String name;
    private final List<FinancialEntityDto> financialItems;

    @JsonCreator
    public UserFinanceDto(
            @JsonProperty("userUuid") String userUuid,
            @JsonProperty("name") String name,
            @JsonProperty("financialItems") List<FinancialEntityDto> financialItems) {
        this.userUuid = userUuid;
        this.name = name;
        this.financialItems = financialItems;
    }
}
