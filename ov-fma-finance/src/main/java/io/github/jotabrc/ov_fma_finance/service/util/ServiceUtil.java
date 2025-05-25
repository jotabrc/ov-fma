package io.github.jotabrc.ov_fma_finance.service.util;

import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public sealed interface ServiceUtil permits ServiceUtilImpl {

    String getUserUuid();
    UserFinance getUserFinance();
    void checkUserAuthorization(@NotNull String userUuid);
    void checkUserAuthorization(@NotNull String userUuid, @NotNull String message);
    boolean ownerMatcher(@NotNull String u1, @NotNull String u2);
}
