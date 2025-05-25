package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.RecurringReceiptDto;
import io.github.jotabrc.ov_fma_finance.model.RecurringReceipt;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.RecurringReceiptRepository;
import io.github.jotabrc.ov_fma_finance.service.util.ServiceUtilImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class RecurringReceiptServiceImplTest {

    @Mock
    private RecurringReceiptRepository recurringReceiptRepository;

    @Mock
    private ServiceUtilImpl serviceUtil;

    @InjectMocks
    private RecurringReceiptServiceImpl recurringReceiptService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addRecurringReceipt() {
        UserFinance userFinance = UserFinance
                .builder()
                .id(1)
                .userUuid(UUID.randomUUID().toString())
                .name("John Doe")
                .financialItems(new ArrayList<>())
                .build();
        RecurringReceipt receipt = new RecurringReceipt(
                1,
                UUID.randomUUID().toString(),
                userFinance,
                LocalDate.now(),
                120.23,
                "Description",
                LocalDateTime.now(),
                null,
                0,
                LocalDate.now(),
                "Vendor"
        );
        doNothing().when(serviceUtil).checkUserAuthorization(anyString());
        when(serviceUtil.getUserFinance()).thenReturn(userFinance);
        when(recurringReceiptRepository.save(any(RecurringReceipt.class))).thenReturn(receipt);
        String uuid = recurringReceiptService.save(userFinance.getUserUuid(), (RecurringReceiptDto) receipt.transform());
        assert uuid.equals(receipt.getUuid());
    }

    @Test
    void updateRecurringReceipt() {
        UserFinance userFinance = UserFinance
                .builder()
                .id(1)
                .userUuid(UUID.randomUUID().toString())
                .name("John Doe")
                .financialItems(new ArrayList<>())
                .build();
        RecurringReceipt receipt = new RecurringReceipt(
                1,
                UUID.randomUUID().toString(),
                userFinance,
                LocalDate.now(),
                120.23,
                "Description",
                LocalDateTime.now(),
                null,
                0,
                LocalDate.now(),
                "Vendor"
        );
        RecurringReceiptDto dto = new RecurringReceiptDto(
                receipt.getUuid(),
                LocalDate.now().plusDays(1),
                139.23,
                "New Description",
                LocalDate.now().plusMonths(2),
                "New Vendor"
        );
        doNothing().when(serviceUtil).checkUserAuthorization(anyString());
        when(recurringReceiptRepository.findByUuid(any())).thenReturn(Optional.of(receipt));
        when(recurringReceiptRepository.save(any(RecurringReceipt.class))).thenReturn(receipt);
        recurringReceiptService.update(userFinance.getUserUuid(), dto);
        assert receipt.getVendor().equals(dto.getVendor());
        assert receipt.getRecurringUntil().equals(dto.getRecurringUntil());
        assert receipt.getDescription().equals(dto.getDescription());
        assert receipt.getAmount() == dto.getAmount();
        assert receipt.getDueDate().equals(dto.getDueDate());
        assert receipt.getUuid().equals(dto.getUuid());
    }

    @Test
    void deleteRecurringReceipt() {
        UserFinance userFinance = UserFinance
                .builder()
                .id(1)
                .userUuid(UUID.randomUUID().toString())
                .name("John Doe")
                .financialItems(new ArrayList<>())
                .build();
        RecurringReceipt receipt = new RecurringReceipt(
                1,
                UUID.randomUUID().toString(),
                userFinance,
                LocalDate.now(),
                120.23,
                "Description",
                LocalDateTime.now(),
                null,
                0,
                LocalDate.now(),
                "Vendor"
        );
        doNothing().when(serviceUtil).checkUserAuthorization(any());
        when(recurringReceiptRepository.findByUuid(any())).thenReturn(Optional.of(receipt));
        doNothing().when(recurringReceiptRepository).delete(any());
        recurringReceiptService.delete(userFinance.getUserUuid(), receipt.getUuid());
    }
}