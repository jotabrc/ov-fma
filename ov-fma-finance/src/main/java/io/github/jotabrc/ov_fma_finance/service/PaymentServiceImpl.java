package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.PaymentDto;
import io.github.jotabrc.ov_fma_finance.handler.PaymentNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.Payment;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.PaymentRepository;
import io.github.jotabrc.ov_fma_finance.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ServiceUtil serviceUtil;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, ServiceUtil serviceUtil) {
        this.paymentRepository = paymentRepository;
        this.serviceUtil = serviceUtil;
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
        UserFinance userFinance = serviceUtil.getUserFinance();

        Payment payment = buildNewPayment(dto, userFinance);
        return paymentRepository.save(payment).getUuid();
    }

    /**
     * Update Payment by ID.
     * @param dto New Payment data.
     */
    @Override
    public void updatePayment(PaymentDto dto) {
        serviceUtil.checkUserAuthorization("Authorization doesn't match with user to be updated");
        Payment payment = getPayment(dto.getId());

        updatePayment(dto, payment);
        paymentRepository.save(payment);
    }

    /**
     * Delete Payment by ID.
     * @param id ID of Payment to be deleted.
     */
    @Override
    public void deletePayment(long id) {
        serviceUtil.checkUserAuthorization("Authorization doesn't match with user to be updated");
        Payment payment = getPayment(id);
        paymentRepository.delete(payment);
    }

    // =================================================================================================================
    // === PRIVATE METHODS ==

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
