package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.ReceiptDto;
import io.github.jotabrc.ov_fma_finance.handler.UserNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.Receipt;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import io.github.jotabrc.ov_fma_finance.repository.ReceiptRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final FinanceRepository financeRepository;

    public ReceiptServiceImpl(ReceiptRepository receiptRepository, FinanceRepository financeRepository) {
        this.receiptRepository = receiptRepository;
        this.financeRepository = financeRepository;
    }

    @Override
    public String addReceipt(final ReceiptDto dto) {
        UserFinance userFinance = financeRepository.findByUserUuid(dto.getUserUuid())
                .orElseThrow(() -> new UserNotFoundException("User with UUID %s not found".formatted(dto.getUserUuid())));

        Receipt receipt = buildNewReceipt(dto, userFinance);
        return receiptRepository.save(receipt).getUuid();
    }

    // =================================================================================================================
    // === PRIVATE METHODS ==

    /**
     * Build new Receipt entity to be persisted.
     * @param dto Receipt data.
     * @return Receipt object.
     */
    private Receipt buildNewReceipt(final ReceiptDto dto, final UserFinance userFinance) {
        return new Receipt(0,
                UUID.randomUUID().toString(), userFinance, dto.getAmount(),
                dto.getDescription(), null, null, 0, dto.getPayee());
    }
}
