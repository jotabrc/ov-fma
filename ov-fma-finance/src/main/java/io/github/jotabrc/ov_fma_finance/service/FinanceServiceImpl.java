package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.*;
import io.github.jotabrc.ov_fma_finance.handler.UserAlreadyExistsException;
import io.github.jotabrc.ov_fma_finance.model.*;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import io.github.jotabrc.ov_fma_finance.service.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Function;

@Service
public final class FinanceServiceImpl implements FinanceService {

    private final FinanceRepository financeRepository;
    private final ServiceUtil serviceUtil;

    @Autowired
    public FinanceServiceImpl(FinanceRepository financeRepository, ServiceUtil serviceUtil) {
        this.financeRepository = financeRepository;
        this.serviceUtil = serviceUtil;
    }

    /**
     * Add new UserFinance Entity. New insertions are received with Kafka on user registration in User service.
     *
     * @param dto UserFinance data.
     */
    @Override
    @Cacheable(value = "user_finance", key = "#dto.getUserUuid")
    public void save(final UserFinanceDto dto) {
        checkUserExistence(dto.getUserUuid());
        UserFinance userFinance = buildNewUserFinance(dto);
        financeRepository.save(userFinance);
    }

    /**
     * Updates UserFinance data.
     *
     * @param dto New data.
     */
    @Override
    @CachePut(value = "user_finance", key = "#dto.getUserUuid")
    public void update(final UserFinanceDto dto) {
        UserFinance userFinance = serviceUtil.getUserFinance();
        updateUserFinance(dto, userFinance);
        financeRepository.save(userFinance);
    }

    @Override
    @Cacheable(value = "financeCache")
    public Page<UserFinanceDto> get(final String userUuid,
                                    final LocalDate fromDate,
                                    final LocalDate toDate,
                                    final int pageStart,
                                    final int pageSize) {
        serviceUtil.checkUserAuthorization(userUuid);
        Pageable pageable = PageRequest.of(pageStart, pageSize, Sort.by("dueDate").descending());
        Page<UserFinance> page = financeRepository.findByDueDate(userUuid, fromDate, toDate, pageable);
        page.forEach(u -> serviceUtil.ownerMatcher(userUuid, u.getUserUuid()));
        return page.map(UserFinance::transform);
    }

    // =================================================================================================================
    // === PRIVATE METHODS ==

    /**
     * Check if User already exists.
     *
     * @param uuid User UUID;
     */
    private void checkUserExistence(final String uuid) {
        boolean exists = financeRepository.existsByUserUuid(uuid);
        if (exists) throw new UserAlreadyExistsException("User with UUID %s already exists".formatted(uuid));
    }

    /**
     * Build new UserFinance entity to be persisted.
     *
     * @param dto UserFinance data.
     * @return UserFinance object.
     */
    private UserFinance buildNewUserFinance(final UserFinanceDto dto) {
        return UserFinance
                .builder()
                .userUuid(dto.getUserUuid())
                .name(dto.getName())
                .financialItems(new ArrayList<>())
                .build();
    }

    /**
     * Update UserFinance data.
     *
     * @param dto         New data.
     * @param userFinance Current UserFinance.
     */
    private void updateUserFinance(final UserFinanceDto dto,
                                   final UserFinance userFinance) {
        userFinance
                .setName(dto.getName());
    }

    @Deprecated(forRemoval = true)
    private UserFinanceDto toDto(final UserFinance user) {
        Function<UserFinance, UserFinanceDto> toDto = u ->
                UserFinanceDto
                        .builder()
                        .userUuid(u.getUserUuid())
                        .name(u.getName())
                        .financialItems(
                                u.getFinancialItems()
                                        .stream()
                                        .map(this::toDto)
                                        .toList()
                        )
                        .build();
        return toDto.apply(user);
    }

    @Deprecated(forRemoval = true)
    private FinancialEntityDto toDto(final FinancialEntity financialEntity) {
        if (financialEntity instanceof Payment e) return toDto(e);
        else if (financialEntity instanceof Receipt e) return toDto(e);
        else if (financialEntity instanceof RecurringPayment e) return toDto(e);
        else if (financialEntity instanceof RecurringReceipt e) return toDto(e);
        else throw new IllegalArgumentException("Unsupported FinancialEntity type %s".formatted(financialEntity.getClass()));
    }

    @Deprecated(forRemoval = true)
    private FinancialEntityDto toDto(final Payment payment) {
        Function<Payment, PaymentDto> toDto = p ->
                new PaymentDto(
                        p.getUuid(),
                        p.getDueDate(),
                        p.getAmount(),
                        p.getDescription(),
                        p.getPayee()
                );
        return toDto.apply(payment);
    }

    @Deprecated(forRemoval = true)
    private FinancialEntityDto toDto(final Receipt receipt) {
        Function<Receipt, ReceiptDto> toDto = r ->
                new ReceiptDto(
                        r.getUuid(),
                        r.getDueDate(),
                        r.getAmount(),
                        r.getDescription(),
                        r.getVendor()
                );
        return toDto.apply(receipt);
    }

    @Deprecated(forRemoval = true)
    private FinancialEntityDto toDto(final RecurringPayment recurringPayment) {
        Function<RecurringPayment, RecurringPaymentDto> toDto = p ->
                new RecurringPaymentDto(
                        p.getUuid(),
                        p.getDueDate(),
                        p.getAmount(),
                        p.getDescription(),
                        p.getRecurringUntil(),
                        p.getPayee()
                );
        return toDto.apply(recurringPayment);
    }

    @Deprecated(forRemoval = true)
    private FinancialEntityDto toDto(final RecurringReceipt recurringReceipt) {
        Function<RecurringReceipt, RecurringReceiptDto> toDto = r ->
                new RecurringReceiptDto(
                        r.getUuid(),
                        r.getDueDate(),
                        r.getAmount(),
                        r.getDescription(),
                        r.getRecurringUntil(),
                        r.getVendor()
                );
        return toDto.apply(recurringReceipt);
    }
}
