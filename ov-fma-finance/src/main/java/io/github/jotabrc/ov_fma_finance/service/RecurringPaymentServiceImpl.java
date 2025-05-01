package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.RecurringPaymentDto;
import io.github.jotabrc.ov_fma_finance.handler.PaymentNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.RecurringPayment;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.RecurringPaymentRepository;
import io.github.jotabrc.ov_fma_finance.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecurringPaymentServiceImpl implements RecurringPaymentService {

    private final RecurringPaymentRepository recurringPaymentRepository;
    private final ServiceUtil serviceUtil;

    @Autowired
    public RecurringPaymentServiceImpl(RecurringPaymentRepository recurringPaymentRepository, ServiceUtil serviceUtil) {
        this.recurringPaymentRepository = recurringPaymentRepository;
        this.serviceUtil = serviceUtil;
    }

    /**
     * Add new RecurringPayment entity.
     * @param dto RecurringPayment data.
     * @return RecurringPayment UUID.
     */
    @Override
    public String addRecurringPayment(final RecurringPaymentDto dto) {
        UserFinance userFinance = serviceUtil.getUserFinance();

        RecurringPayment payment = buildNewRecurringPayment(dto, userFinance);
        return recurringPaymentRepository.save(payment).getUuid();
    }

    /**
     * Update RecurringPayment data.
     * @param dto New data.
     */
    @Override
    public void updateRecurringPayment(final RecurringPaymentDto dto) {
        serviceUtil.checkUserAuthorization("Authorization doesn't match with user to be updated");
        RecurringPayment recurringPayment = getRecurringPayment(dto.getId());

        updateRecurringPayment(dto, recurringPayment);
        recurringPaymentRepository.save(recurringPayment);
    }

    /**
     * Delete RecurringPayment by id.
     * @param id ID of RecurringPayment to be deleted.
     */
    @Override
    public void deleteRecurringPayment(final long id) {
        serviceUtil.checkUserAuthorization("Authorization doesn't match with user to be updated");
        RecurringPayment recurringPayment = getRecurringPayment(id);
        recurringPaymentRepository.delete(recurringPayment);
    }

    // =================================================================================================================
    // === PRIVATE METHODS ==

    /**
     * Build new RecurringPayment entity to be persisted.
     * @param dto RecurringPayment data.
     * @return RecurringPayment object.
     */
    private RecurringPayment buildNewRecurringPayment(final RecurringPaymentDto dto, final UserFinance userFinance) {
        return new RecurringPayment(0,
                UUID.randomUUID().toString(), userFinance, dto.getAmount(),
                dto.getDescription(), null, null, 0, dto.getRecurringUntil(), dto.getPayee());
    }

    /**
     * Get RecurringPayment by ID.
     * @param id RecurringPayment ID.
     * @return RecurringPayment.
     */
    private RecurringPayment getRecurringPayment(final long id) {
        return recurringPaymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with ID %d not found for".formatted(id)));
    }

    /**
     * Updates RecurringPayment with new data.
     * @param dto New data.
     * @param recurringPayment Current data to be updated.
     */
    private void updateRecurringPayment(final RecurringPaymentDto dto, final RecurringPayment recurringPayment) {
        recurringPayment
                .setAmount(dto.getAmount())
                .setDescription(dto.getDescription());
        recurringPayment.setPayee(dto.getPayee());
    }
}
