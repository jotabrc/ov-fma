package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.PaymentDto;
import io.github.jotabrc.ov_fma_finance.dto.UserFinanceDto;
import io.github.jotabrc.ov_fma_finance.handler.UserAlreadyExistsException;
import io.github.jotabrc.ov_fma_finance.handler.UserNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.Payment;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class FinanceServiceImpl implements FinanceService {

    private final FinanceRepository financeRepository;

    @Autowired
    public FinanceServiceImpl(FinanceRepository financeRepository) {
        this.financeRepository = financeRepository;
    }

    /**
     * Add new UserFinance Entity. New insertions are received with Kafka on user registration in User service.
     * @param dto UserFinance data.
     */
    @Override
    public void addUserFinance(final UserFinanceDto dto) {
        checkUserExistence(dto.getUserUuid());

        UserFinance userFinance = buildNewUserFinance(dto);
        financeRepository.save(userFinance);
    }

    /**
     * Updates UserFinance data.
     * @param dto New data.
     */
    @Override
    public void updateUserFinance(UserFinanceDto dto) {
        UserFinance userFinance = financeRepository.findByUserUuid(dto.getUserUuid())
                .orElseThrow(() -> new UserNotFoundException("User with UUID %s not found"));

        updateUserFinance(dto, userFinance);

        financeRepository.save(userFinance);
    }

    // =================================================================================================================
    // === PRIVATE METHODS ==

    /**
     * Check if User already exists.
     * @param uuid User UUID;
     */
    private void checkUserExistence(final String uuid) {
        boolean exists = financeRepository.existsByUserUuid(uuid);
        if (exists) throw new UserAlreadyExistsException("User with UUID %s already exists".formatted(uuid));
    }

    /**
     * Build new UserFinance entity to be persisted.
     * @param dto UserFinance data.
     * @return UserFinance object.
     */
    private UserFinance buildNewUserFinance(final UserFinanceDto dto) {
        return UserFinance
                .builder()
                .userUuid(dto.getUserUuid())
                .username(dto.getUsername())
                .name(dto.getName())
                .email(dto.getEmail())
                .isActive(dto.isActive())
                .financialItems(new ArrayList<>())
                .build();
    }

    /**
     * Build new Payment entity to be persisted.
     * @param dto Payment data.
     * @return Payment object.
     */
    private Payment buildNewPayment(final PaymentDto dto) {
        return new Payment(0,
                UUID.randomUUID().toString(), null, dto.getAmount(),
                dto.getDescription(), null, null, 0, dto.getVendor());
    }

    /**
     * Update UserFinance data.
     * @param dto New data.
     * @param userFinance Current UserFinance.
     */
    private void updateUserFinance(UserFinanceDto dto, UserFinance userFinance) {
        userFinance
                .setUsername(dto.getUsername())
                .setEmail(dto.getEmail())
                .setName(dto.getName())
                .setActive(dto.isActive());
    }
}
