package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.RecurringReceiptDto;
import io.github.jotabrc.ov_fma_finance.handler.ReceiptNotFoundException;
import io.github.jotabrc.ov_fma_finance.handler.UserNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.RecurringReceipt;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import io.github.jotabrc.ov_fma_finance.repository.RecurringReceiptRepository;
import io.github.jotabrc.ov_fma_finance.util.ServiceUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecurringReceiptServiceImpl implements RecurringReceiptService {

    private final RecurringReceiptRepository recurringReceiptRepository;
    private final FinanceRepository financeRepository;
    private final ServiceUtil serviceUtil;

    public RecurringReceiptServiceImpl(RecurringReceiptRepository recurringReceiptRepository, FinanceRepository financeRepository, ServiceUtil serviceUtil) {
        this.recurringReceiptRepository = recurringReceiptRepository;
        this.financeRepository = financeRepository;
        this.serviceUtil = serviceUtil;
    }

    /**
     * Add new RecurringReceipt to the logged-in user (Authorization in SecurityContextHolder subject UUID).
     * @param dto RecurringReceipt data.
     * @return RecurringReceipt UUID.
     */
    @Override
    public String addRecurringReceipt(final RecurringReceiptDto dto) {
        final String uuid = serviceUtil.getUserUuid();
        UserFinance userFinance = financeRepository.findByUserUuid(uuid)
                .orElseThrow(() -> new UserNotFoundException("User with UUID %s not found".formatted(uuid)));

        RecurringReceipt receipt = buildNewRecurringReceipt(dto, userFinance);
        return recurringReceiptRepository.save(receipt).getUuid();
    }

    /**
     * Update RecurringReceipt by ID.
     * @param dto New RecurringReceipt data.
     */
    @Override
    public void updateRecurringReceipt(RecurringReceiptDto dto) {
        serviceUtil.checkUserAuthorization();
        RecurringReceipt receipt = getRecurringReceipt(dto.getId());
        updateRecurringReceipt(dto, receipt);
        recurringReceiptRepository.save(receipt);
    }

    /**
     * Delete RecurringReceipt by ID.
     * @param id ID of RecurringReceipt to be deleted.
     */
    @Override
    public void deleteRecurringReceipt(long id) {
        serviceUtil.checkUserAuthorization();
        RecurringReceipt receipt = getRecurringReceipt(id);
        recurringReceiptRepository.delete(receipt);
        recurringReceiptRepository.save(receipt);
    }

    // =================================================================================================================
    // === PRIVATE METHODS ==

    /**
     * Build new Receipt entity to be persisted.
     * @param dto Receipt data.
     * @return Receipt object.
     */
    private RecurringReceipt buildNewRecurringReceipt(final RecurringReceiptDto dto, final UserFinance userFinance) {
        return new RecurringReceipt(0,
                UUID.randomUUID().toString(),
                userFinance,
                dto.getAmount(),
                dto.getDescription(),
                null,
                null,
                0,
                dto.getDay(),
                dto.getMonth(),
                dto.getYear(),
                dto.getVendor());
    }

    /**
     * Get RecurringReceipt by ID.
     * @param id RecurringReceipt ID.
     * @return RecurringReceipt.
     */
    private RecurringReceipt getRecurringReceipt(final long id) {
        return recurringReceiptRepository.findById(id)
                .orElseThrow(() -> new ReceiptNotFoundException("Recurring Receipt with ID %d not found".formatted(id)));
    }

    /**
     * Update RecurringReceipt with new data.
     * @param dto New RecurringReceipt data.
     * @param receipt Current RecurringReceipt data.
     */
    private void updateRecurringReceipt(final RecurringReceiptDto dto, final RecurringReceipt receipt) {
        receipt
                .setAmount(dto.getAmount())
                .setDescription(dto.getDescription());
        receipt
                .setVendor(dto.getVendor())
                .setDay(dto.getDay())
                .setMonth(dto.getMonth())
                .setMonth(dto.getMonth());

    }
}
