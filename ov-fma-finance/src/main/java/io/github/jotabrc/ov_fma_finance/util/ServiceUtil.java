package io.github.jotabrc.ov_fma_finance.util;

import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ServiceUtil {

    UserFinance getUserFinance();
    void checkUserAuthorization();
    void checkUserAuthorization(@NotNull String message);
}
