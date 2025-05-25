package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.RecurringPaymentDto;
import io.github.jotabrc.ov_fma_finance.handler.PaymentNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.RecurringPayment;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.RecurringPaymentRepository;
import io.github.jotabrc.ov_fma_finance.service.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public final class RecurringPaymentServiceImpl implements RecurringPaymentService {

    private final RecurringPaymentRepository recurringPaymentRepository;
    private final ServiceUtil serviceUtil;

    @Autowired
    public RecurringPaymentServiceImpl(RecurringPaymentRepository recurringPaymentRepository, ServiceUtil serviceUtil) {
        this.recurringPaymentRepository = recurringPaymentRepository;
        this.serviceUtil = serviceUtil;
    }

    /**
     * Add new RecurringPayment entity.
     *
     * @param dto RecurringPayment data.
     * @return RecurringPayment UUID.
     */
    @Override
    public String save(final String userUuid, final RecurringPaymentDto dto) {
        serviceUtil.checkUserAuthorization(userUuid);
        UserFinance userFinance = serviceUtil.getUserFinance();
        RecurringPayment payment = buildNewRecurringPayment(dto, userFinance);
        return recurringPaymentRepository.save(payment).getUuid();
    }

    /**
     * Update RecurringPayment data.
     *
     * @param dto New data.
     */
    @Override
    public void update(final String userUuid, final RecurringPaymentDto dto) {
        serviceUtil.checkUserAuthorization(userUuid);
        RecurringPayment recurringPayment = getRecurringPayment(dto.getUuid());
        serviceUtil.ownerMatcher(userUuid, recurringPayment.getUserFinance().getUserUuid());
        updateRecurringPayment(dto, recurringPayment);
        recurringPaymentRepository.save(recurringPayment);
    }

    /**
     * Delete RecurringPayment.
     *
     * @param uuid UUID of RecurringPayment to be deleted.
     */
    @Override
    public void delete(final String userUuid, final String uuid) {
        serviceUtil.checkUserAuthorization(userUuid);
        RecurringPayment recurringPayment = getRecurringPayment(uuid);
        serviceUtil.ownerMatcher(userUuid, recurringPayment.getUserFinance().getUserUuid());
        recurringPaymentRepository.delete(recurringPayment);
    }

    // =================================================================================================================
    // === PRIVATE METHODS ==

    /**
     * Build new RecurringPayment entity to be persisted.
     *
     * @param dto RecurringPayment data.
     * @return RecurringPayment object.
     */
    private RecurringPayment buildNewRecurringPayment(final RecurringPaymentDto dto, final UserFinance userFinance) {
        return new RecurringPayment(0,
                UUID.randomUUID().toString(),
                userFinance,
                dto.getDueDate(),
                dto.getAmount(),
                dto.getDescription(),
                null,
                null,
                0,
                dto.getRecurringUntil(),
                dto.getPayee());
    }

    /**
     * Get RecurringPayment.
     *
     * @param uuid RecurringPayment UUID.
     * @return RecurringPayment.
     */
    private RecurringPayment getRecurringPayment(final String uuid) {
        return recurringPaymentRepository.findByUuid(uuid)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with UUID %s not found for".formatted(uuid)));
    }

    /**
     * Updates RecurringPayment with new data.
     *
     * @param dto              New data.
     * @param recurringPayment Current data to be updated.
     */
    private void updateRecurringPayment(final RecurringPaymentDto dto, final RecurringPayment recurringPayment) {
        recurringPayment
                .setDueDate(dto.getDueDate())
                .setAmount(dto.getAmount())
                .setDescription(dto.getDescription());

        recurringPayment
                .setPayee(dto.getPayee())
                .setRecurringUntil(dto.getRecurringUntil());
    }
}
