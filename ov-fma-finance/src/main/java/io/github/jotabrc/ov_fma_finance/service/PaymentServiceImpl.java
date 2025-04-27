package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.PaymentDto;
import io.github.jotabrc.ov_fma_finance.handler.UserNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.Payment;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import io.github.jotabrc.ov_fma_finance.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        UserFinance userFinance = financeRepository.findByUserUuid(dto.getUserUuid())
                .orElseThrow(() -> new UserNotFoundException("User with UUID %s not found".formatted(dto.getUserUuid())));

        Payment payment = buildNewPayment(dto, userFinance);
        return paymentRepository.save(payment).getUuid();
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
                dto.getDescription(), null, null, 0, dto.getVendor());
    }
}
