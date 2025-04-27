package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.RecurringPaymentDto;
import io.github.jotabrc.ov_fma_finance.handler.PartyNotFoundException;
import io.github.jotabrc.ov_fma_finance.handler.UserNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.Party;
import io.github.jotabrc.ov_fma_finance.model.RecurringPayment;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import io.github.jotabrc.ov_fma_finance.repository.PartyRepository;
import io.github.jotabrc.ov_fma_finance.repository.RecurringPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecurringPaymentServiceImpl implements RecurringPaymentService {

    private final RecurringPaymentRepository recurringPaymentRepository;
    private final FinanceRepository financeRepository;
    private final PartyRepository partyRepository;

    @Autowired
    public RecurringPaymentServiceImpl(RecurringPaymentRepository recurringPaymentRepository, FinanceRepository financeRepository, PartyRepository partyRepository) {
        this.recurringPaymentRepository = recurringPaymentRepository;
        this.financeRepository = financeRepository;
        this.partyRepository = partyRepository;
    }

    /**
     * Add new RecurringPayment entity.
     * @param dto RecurringPayment data.
     * @return RecurringPayment UUID.
     */
    @Override
    public String addRecurringPayment(final RecurringPaymentDto dto) {
        UserFinance userFinance = financeRepository.findByUserUuid(dto.getUserUuid())
                .orElseThrow(() -> new UserNotFoundException("User with UUID %s not found".formatted(dto.getUserUuid())));

        RecurringPayment payment = buildNewRecurringPayment(dto, userFinance);
        return recurringPaymentRepository.save(payment).getUuid();
    }

    // =================================================================================================================
    // === PRIVATE METHODS ==

    /**
     * Build new RecurringPayment entity to be persisted.
     * @param dto RecurringPayment data.
     * @return RecurringPayment object.
     */
    private RecurringPayment buildNewRecurringPayment(final RecurringPaymentDto dto, final UserFinance userFinance) {
        Party party = getPartyOrThrow(dto.getPayee().getName());
        return new RecurringPayment(0,
                UUID.randomUUID().toString(), userFinance, dto.getAmount(),
                dto.getDescription(), null, null, 0, dto.getRecurringUntil(), party);
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
