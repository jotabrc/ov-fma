package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.ReceiptDto;
import io.github.jotabrc.ov_fma_finance.handler.ReceiptNotFoundException;
import io.github.jotabrc.ov_fma_finance.model.Receipt;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.ReceiptRepository;
import io.github.jotabrc.ov_fma_finance.util.ServiceUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ServiceUtil serviceUtil;

    public ReceiptServiceImpl(ReceiptRepository receiptRepository, ServiceUtil serviceUtil) {
        this.receiptRepository = receiptRepository;
        this.serviceUtil = serviceUtil;
    }

    /**
     * Add new Receipt to the logged-in user (Authorization in SecurityContextHolder subject UUID).
     * @param dto Receipt data.
     * @return Receipt UUID.
     */
    @Override
    public String addReceipt(final ReceiptDto dto) {
        UserFinance userFinance = serviceUtil.getUserFinance();

        Receipt receipt = buildNewReceipt(dto, userFinance);
        return receiptRepository.save(receipt).getUuid();
    }

    /**
     * Update Receipt with ID.
     * @param dto New Receipt data.
     */
    @Override
    public void updateReceipt(ReceiptDto dto) {
        serviceUtil.checkUserAuthorization();
        Receipt receipt = getReceipt(dto.getId());
        updateReceipt(dto, receipt);
        receiptRepository.save(receipt);
    }

    /**
     * Delete Receipt with ID.
     * @param id ID of Receipt to be deleted.
     */
    @Override
    public void deleteReceipt(long id) {
        serviceUtil.checkUserAuthorization();
        Receipt receipt = getReceipt(id);
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
                UUID.randomUUID().toString(), userFinance, dto.getAmount(),
                dto.getDescription(), null, null, 0, dto.getVendor());
    }

    /**
     * Get Receipt with ID.
     * @param id Receipt id.
     * @return Receipt.
     */
    private Receipt getReceipt(final long id) {
        return receiptRepository.findById(id)
                .orElseThrow(() -> new ReceiptNotFoundException("Receipt with ID %d not found".formatted(id)));
    }

    /**
     * Updates Receipt with new data.
     * @param dto New Receipt data.
     * @param receipt Current Receipt data.
     */
    private void updateReceipt(final ReceiptDto dto, final Receipt receipt) {
        receipt
                .setAmount(dto.getAmount())
                .setDescription(dto.getDescription());
        receipt.setVendor(dto.getVendor());
    }
}
