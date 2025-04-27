package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.ReceiptDto;
import io.github.jotabrc.ov_fma_finance.handler.PartyNotFoundException;
import io.github.jotabrc.ov_fma_finance.handler.UserNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.Party;
import io.github.jotabrc.ov_fma_finance.model.Receipt;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import io.github.jotabrc.ov_fma_finance.repository.PartyRepository;
import io.github.jotabrc.ov_fma_finance.repository.ReceiptRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final FinanceRepository financeRepository;
    private final PartyRepository partyRepository;

    public ReceiptServiceImpl(ReceiptRepository receiptRepository, FinanceRepository financeRepository, PartyRepository partyRepository) {
        this.receiptRepository = receiptRepository;
        this.financeRepository = financeRepository;
        this.partyRepository = partyRepository;
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
        Party party = getPartyOrThrow(dto.getPayee().getName());
        return new Receipt(0,
                UUID.randomUUID().toString(), userFinance, dto.getAmount(),
                dto.getDescription(), null, null, 0, party);
    }

    /**
     * Get Party information, or throw exception if no found.
     * @param name Vendor name.
     * @return Party object.
     */
    private Party getPartyOrThrow(final String name) {
        return partyRepository.findByName(name)
                .orElseThrow(() -> new PartyNotFoundException("Vendor not found with name %s".formatted(name)));
    }
}
