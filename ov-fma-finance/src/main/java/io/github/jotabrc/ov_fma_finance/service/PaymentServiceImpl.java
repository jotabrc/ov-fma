package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.PaymentDto;
import io.github.jotabrc.ov_fma_finance.handler.PaymentNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.Payment;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.PaymentRepository;
import io.github.jotabrc.ov_fma_finance.service.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public final class PaymentServiceImpl implements PaymentService {

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
    public String save(final String userUuid, final PaymentDto dto) {
        serviceUtil.checkUserAuthorization(userUuid);
        UserFinance userFinance = serviceUtil.getUserFinance();
        Payment payment = buildNewPayment(dto, userFinance);
        return paymentRepository.save(payment).getUuid();
    }

    /**
     * Update Payment.
     * @param dto New Payment data.
     */
    @Override
    public void update(final String userUuid, final PaymentDto dto) {
        serviceUtil.checkUserAuthorization(userUuid);
        Payment payment = getPayment(dto.getUuid());
        serviceUtil.ownerMatcher(userUuid, payment.getUserFinance().getUserUuid());
        updatePayment(dto, payment);
        paymentRepository.save(payment);
    }

    /**
     * Delete Payment.
     * @param uuid UUID of Payment to be deleted.
     */
    @Override
    public void delete(final String userUuid, final String uuid) {
        serviceUtil.checkUserAuthorization(userUuid);
        Payment payment = getPayment(uuid);
        serviceUtil.ownerMatcher(userUuid, payment.getUserFinance().getUserUuid());
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
                UUID.randomUUID().toString(),
                userFinance,
                dto.getDueDate(),
                dto.getAmount(),
                dto.getDescription(),
                null,
                null,
                0,
                dto.getPayee());
    }

    /**
     * Update Paymento with new data.
     * @param dto New Payment data.
     * @param payment Current Payment data to be updated.
     */
    private void updatePayment(final PaymentDto dto, final Payment payment) {
        payment
                .setDueDate(dto.getDueDate())
                .setAmount(dto.getAmount())
                .setDescription(dto.getDescription());
        payment.setPayee(dto.getPayee());
    }

    /**
     * Get Payment by UUID.
     * @param uuid Payment UUID.
     * @return Payment.
     */
    private Payment getPayment(final String uuid) {
        return paymentRepository.findByUuid(uuid)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with UUID %s not found for".formatted(uuid)));
    }
}
