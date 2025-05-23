package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.ReceiptDto;
import io.github.jotabrc.ov_fma_finance.handler.ReceiptNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.Receipt;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import io.github.jotabrc.ov_fma_finance.repository.ReceiptRepository;
import io.github.jotabrc.ov_fma_finance.util.ServiceUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final FinanceRepository financeRepository;
    private final ServiceUtil serviceUtil;

    public ReceiptServiceImpl(ReceiptRepository receiptRepository, FinanceRepository financeRepository, ServiceUtil serviceUtil) {
        this.receiptRepository = receiptRepository;
        this.financeRepository = financeRepository;
        this.serviceUtil = serviceUtil;
    }

    /**
     * Add new Receipt to the logged-in user (Authorization in SecurityContextHolder subject UUID).
     * @param dto Receipt data.
     * @return Receipt UUID.
     */
    @Override
    public String addReceipt(final ReceiptDto dto) {
        serviceUtil.checkUserAuthorization(dto.getUuid());
        UserFinance userFinance = serviceUtil.getUserFinance();

        Receipt receipt = buildNewReceipt(dto, userFinance);
        return receiptRepository.save(receipt).getUuid();
    }

    /**
     * Update Receipt.
     * @param dto New Receipt data.
     */
    @Override
    public void updateReceipt(final ReceiptDto dto) {
        serviceUtil.checkUserAuthorization(dto.getUuid());
        Receipt receipt = getReceipt(dto.getUuid());
        updateReceipt(dto, receipt);
        receiptRepository.save(receipt);
    }

    /**
     * Delete Receipt with UUID.
     * @param uuid UUID of Receipt to be deleted.
     */
    @Override
    public void deleteReceipt(final String uuid) {
        serviceUtil.checkUserAuthorization(uuid);
        Receipt receipt = getReceipt(uuid);
        receiptRepository.delete(receipt);
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
                UUID.randomUUID().toString(),
                userFinance,
                dto.getDueDate(),
                dto.getAmount(),
                dto.getDescription(),
                null,
                null,
                0,
                dto.getVendor());
    }

    /**
     * Get Receipt with UUID.
     *
     * @param uuid Receipt UUID.
     * @return Receipt.
     */
    private Receipt getReceipt(final String uuid) {
        return receiptRepository.findByUuid(uuid)
                .orElseThrow(() -> new ReceiptNotFoundException("Receipt with UUID %s not found".formatted(uuid)));
    }

    /**
     * Updates Receipt with new data.
     * @param dto New Receipt data.
     * @param receipt Current Receipt data.
     */
    private void updateReceipt(final ReceiptDto dto, final Receipt receipt) {
        receipt
                .setDueDate(dto.getDueDate())
                .setAmount(dto.getAmount())
                .setDescription(dto.getDescription());
        receipt.setVendor(dto.getVendor());
    }
}
