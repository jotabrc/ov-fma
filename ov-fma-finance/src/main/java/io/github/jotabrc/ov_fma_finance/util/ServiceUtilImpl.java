package io.github.jotabrc.ov_fma_finance.util;

import io.github.jotabrc.ov_fma_finance.handler.UnauthorizedException;
import io.github.jotabrc.ov_fma_finance.handler.UserNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ServiceUtilImpl implements ServiceUtil {

    private final FinanceRepository financeRepository;

    @Autowired
    public ServiceUtilImpl(FinanceRepository financeRepository) {
        this.financeRepository = financeRepository;
    }

    /**
     * Get User UUID from the SecurityContextHolder Authentication.
     * @return UUID.
     */
    @Override
    public String getUserUuid() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Get UserFinance by UUID.
     * @return UserFinance data.
     */
    @Cacheable(value = "user_finance", key = "#result.userUuid")
    @Override
    public UserFinance getUserFinance() {
        String userUuid = getUserUuid();
        return financeRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User with UUID %s not found".formatted(userUuid)));
    }

    /**
     * Check User Authorization and call overridden method with a default message.
     */
    @Override
    public void checkUserAuthorization() {
        checkUserAuthorization("User authorization denied");
    }

    /**
     * Check User Authorization and throws exception if denied.
     * @param message Exception message.
     * @throws UnauthorizedException
     */
    @Override
    public void checkUserAuthorization(final String message) {
        String userUuid = getUserUuid();
        boolean isValid = SecurityContextHolder.getContext().getAuthentication().getName().equals(userUuid);
        if (!isValid) throw new UnauthorizedException(message);
    }
}
