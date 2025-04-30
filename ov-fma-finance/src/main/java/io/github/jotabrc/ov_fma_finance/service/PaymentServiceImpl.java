package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.PaymentDto;
import io.github.jotabrc.ov_fma_finance.handler.PaymentNotFoundException;
import io.github.jotabrc.ov_fma_finance.handler.UnauthorizedException;
import io.github.jotabrc.ov_fma_finance.handler.UserNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.Payment;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import io.github.jotabrc.ov_fma_finance.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final FinanceRepository financeRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, FinanceRepository financeRepository) {
        this.paymentRepository = paymentRepository;
        this.financeRepository = financeRepository;
    }

    /**
     * Add new Payment entity.
     *
     * @param dto Payment data.
     * @return Payment UUID.
     */
    @Override
    public String addPayment(final PaymentDto dto) {
        // Uses SecurityContextHolder Authorization subject (UUID) to find UserFinance
        // doesn't require to check user authorization for updating this information
        UserFinance userFinance = getUserFinance();

        Payment payment = buildNewPayment(dto, userFinance);
        return paymentRepository.save(payment).getUuid();
    }

    @Override
    public void updatePayment(PaymentDto dto) {
        checkUserAuthorization("Authorization doesn't match with user to be updated");
        Payment payment = getPayment(dto.getId());

        updatePayment(dto, payment);
        paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(long id) {
        checkUserAuthorization("Authorization doesn't match with user to be updated");
        Payment payment = getPayment(id);

        paymentRepository.delete(payment);
    }

    // =================================================================================================================
    // === PRIVATE METHODS ==

    /**
     * Get User UUID from the SecurityContextHolder Authentication.
     * @return UUID.
     */
    private String getUserUuid() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Get UserFinance by UUID.
     * @return UserFinance data.
     */
    private UserFinance getUserFinance() {
        String userUuid = getUserUuid();
        return financeRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new UserNotFoundException("User with UUID %s not found".formatted(userUuid)));
    }

    /**
     * Check User Authorization and call overridden method with a default message.
     */
    private void checkUserAuthorization() {
        checkUserAuthorization("User authorization denied");
    }

    /**
     * Check User Authorization and throws exception if denied.
     * @param message Exception message.
     * @throws UnauthorizedException
     */
    private void checkUserAuthorization(final String message) {
        String userUuid = getUserUuid();
        boolean isValid = SecurityContextHolder.getContext().getAuthentication().getName().equals(userUuid);
        if (!isValid) throw new UnauthorizedException(message);
    }

    /**
     * Build new Payment entity to be persisted.
     * @param dto Payment data.
     * @return Payment object.
     */
    private Payment buildNewPayment(final PaymentDto dto, final UserFinance userFinance) {
        return new Payment(0,
                UUID.randomUUID().toString(), userFinance, dto.getAmount(),
                dto.getDescription(), null, null, 0, dto.getPayee());
    }

    /**
     * Update Paymento with new data.
     * @param dto New Payment data.
     * @param payment Current Payment data to be updated.
     */
    private void updatePayment(final PaymentDto dto, final Payment payment) {
        payment
                .setAmount(dto.getAmount())
                .setDescription(dto.getDescription());
        payment.setPayee(dto.getPayee());
    }

    /**
     * Get Payment by ID.
     * @param id FinancialEntity / Payment ID.
     * @return Payment.
     */
    private Payment getPayment(final long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with ID %d not found for".formatted(id)));
    }
}
