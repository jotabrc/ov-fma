package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.RecurringReceiptDto;
import io.github.jotabrc.ov_fma_finance.handler.UserNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.RecurringReceipt;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import io.github.jotabrc.ov_fma_finance.repository.RecurringReceiptRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecurringReceiptServiceImpl implements RecurringReceiptService {

    private final RecurringReceiptRepository recurringReceiptRepository;
    private final FinanceRepository financeRepository;

    public RecurringReceiptServiceImpl(RecurringReceiptRepository recurringReceiptRepository, FinanceRepository financeRepository) {
        this.recurringReceiptRepository = recurringReceiptRepository;
        this.financeRepository = financeRepository;
    }

    @Override
    public String addRecurringReceipt(final RecurringReceiptDto dto) {
        UserFinance userFinance = financeRepository.findByUserUuid(dto.getUserUuid())
                .orElseThrow(() -> new UserNotFoundException("User with UUID %s not found".formatted(dto.getUserUuid())));

        RecurringReceipt receipt = buildNewRecurringReceipt(dto, userFinance);
        return recurringReceiptRepository.save(receipt).getUuid();
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
                UUID.randomUUID().toString(), userFinance, dto.getAmount(),
                dto.getDescription(), null, null, 0, dto.getRecurringUntil(), dto.getVendor());
    }
}
