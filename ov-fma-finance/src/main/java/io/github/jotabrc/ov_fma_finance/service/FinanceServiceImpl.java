package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.*;
import io.github.jotabrc.ov_fma_finance.handler.InstanceNotCompatibleException;
import io.github.jotabrc.ov_fma_finance.handler.UserAlreadyExistsException;
import io.github.jotabrc.ov_fma_finance.handler.UserNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.*;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.function.Function;

@Service
public class FinanceServiceImpl implements FinanceService {

    private final FinanceRepository financeRepository;

    @Autowired
    public FinanceServiceImpl(FinanceRepository financeRepository) {
        this.financeRepository = financeRepository;
    }

    /**
     * Add new UserFinance Entity. New insertions are received with Kafka on user registration in User service.
     *
     * @param dto UserFinance data.
     * @return UserFinanceDto
     */
    @Override
    @Cacheable(value = "user_finance", key = "#dto.getUserUuid")
    public UserFinanceDto addUserFinance(final UserFinanceDto dto) {
        checkUserExistence(dto.getUserUuid());

        UserFinance userFinance = buildNewUserFinance(dto);
        financeRepository.save(userFinance);
        return toDto(userFinance);
    }

    /**
     * Updates UserFinance data.
     *
     * @param dto New data.
     * @return UserFinanceDto
     */
    @Override
    @CachePut(value = "user_finance", key = "#dto.getUserUuid")
    public UserFinanceDto updateUserFinance(UserFinanceDto dto) {
        UserFinance userFinance = financeRepository.findByUserUuid(dto.getUserUuid())
                .orElseThrow(() -> new UserNotFoundException("User with UUID %s not found"));

        updateUserFinance(dto, userFinance);
        financeRepository.save(userFinance);
        return toDto(userFinance);
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
                .username(dto.getUsername())
                .email(dto.getEmail())
                .name(dto.getName())
                .isActive(dto.isActive())
                .financialItems(new ArrayList<>())
                .build();
    }

    /**
     * Update UserFinance data.
     *
     * @param dto         New data.
     * @param userFinance Current UserFinance.
     */
    private void updateUserFinance(UserFinanceDto dto, UserFinance userFinance) {
        userFinance
                .setUsername(dto.getUsername())
                .setEmail(dto.getEmail())
                .setActive(dto.isActive());
    }

    private UserFinanceDto toDto(final UserFinance user) {
        Function<UserFinance, UserFinanceDto> toDto = u ->
                UserFinanceDto
                        .builder()
                        .userUuid(u.getUserUuid())
                        .username(u.getUsername())
                        .email(u.getEmail())
                        .isActive(u.isActive())
                        .financialItems(
                                u.getFinancialItems()
                                        .stream()
                                        .map(this::toDto)
                                        .toList()
                        )
                        .build();
        return toDto.apply(user);
    }

    private FinancialEntityDto toDto(final FinancialEntity financialEntity) {
        if (financialEntity instanceof Payment e) return toDto(e);
        else if (financialEntity instanceof Receipt e) return toDto(e);
        else if (financialEntity instanceof RecurringPayment e) return toDto(e);
        else if (financialEntity instanceof RecurringReceipt e) return toDto(e);
        else throw new InstanceNotCompatibleException("Unsupported FinancialEntity type %s".formatted(financialEntity.getClass()));
    }

    private FinancialEntityDto toDto(final Payment payment) {
        Function<Payment, PaymentDto> toDto = p ->
                new PaymentDto(
                        p.getId(),
                        p.getAmount(),
                        p.getDescription(),
                        p.getPayee()
                );
        return toDto.apply(payment);
    }

    private FinancialEntityDto toDto(final Receipt receipt) {
        Function<Receipt, ReceiptDto> toDto = r ->
                new ReceiptDto(
                        r.getId(),
                        r.getAmount(),
                        r.getDescription(),
                        r.getVendor()
                );
        return toDto.apply(receipt);
    }

    private FinancialEntityDto toDto(final RecurringPayment recurringPayment) {
        Function<RecurringPayment, RecurringPaymentDto> toDto = p ->
                new RecurringPaymentDto(
                        p.getId(),
                        p.getAmount(),
                        p.getDescription(),
                        p.getDay(),
                        p.getMonth(),
                        p.getYear(),
                        p.getPayee()
                );
        return toDto.apply(recurringPayment);
    }

    private FinancialEntityDto toDto(final RecurringReceipt recurringReceipt) {
        Function<RecurringReceipt, RecurringReceiptDto> toDto = p ->
                new RecurringReceiptDto(
                        p.getId(),
                        p.getAmount(),
                        p.getDescription(),
                        p.getDay(),
                        p.getMonth(),
                        p.getYear(),
                        p.getVendor()
                );
        return toDto.apply(recurringReceipt);
    }
}
