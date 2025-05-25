package io.github.jotabrc.ov_fma_finance.service.util;

import io.github.jotabrc.ov_fma_finance.handler.UnauthorizedException;
import io.github.jotabrc.ov_fma_finance.handler.UserNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.function.BiPredicate;

@Service
public final class ServiceUtilImpl implements ServiceUtil {

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
     * @param userUuid User uuid.
     */
    @Override
    public void checkUserAuthorization(final String userUuid) {
        checkUserAuthorization(userUuid, "User authorization denied");
    }

    /**
     * Check User Authorization and throws exception if denied.
     * @param userUuid User uuid.
     * @param message Exception message.
     * @throws UnauthorizedException
     */
    @Override
    public void checkUserAuthorization(final String userUuid, final String message) {
        boolean isValid = getUserUuid().equals(userUuid);
        if (!isValid) throw new UnauthorizedException(message);
    }

    /**
     * Checks User Ownership UUID (u1) with entity UUID (u2)
     * @param u1 User UUID.
     * @param u2 Entity owner user UUID.
     */
    @Override
    public boolean ownerMatcher(String u1, String u2) {
        BiPredicate<String, String> matches = String::equals;
        if (!matches.test(u1, u2)) throw  new UnauthorizedException("User authorization denied to modify requested data, owner doesn't match");
        return true;
    }
}
