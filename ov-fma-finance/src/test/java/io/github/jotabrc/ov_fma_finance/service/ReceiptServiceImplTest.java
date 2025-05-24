package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.ReceiptDto;
import io.github.jotabrc.ov_fma_finance.model.Receipt;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.ReceiptRepository;
import io.github.jotabrc.ov_fma_finance.util.ServiceUtilImpl;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class ReceiptServiceImplTest {

    @Mock
    ReceiptRepository receiptRepository;

    @Mock
    ServiceUtilImpl serviceUtil;

    @InjectMocks
    ReceiptServiceImpl receiptService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addReceipt() {
        UserFinance userFinance = UserFinance
                .builder()
                .id(1)
                .userUuid(UUID.randomUUID().toString())
                .username("username")
                .email("email@email.com")
                .name("John Doe")
                .isActive(true)
                .financialItems(new ArrayList<>())
                .build();
        Receipt receipt = new Receipt(
                1,
                UUID.randomUUID().toString(),
                userFinance,
                LocalDate.now(),
                1000.00,
                "Description",
                LocalDateTime.now(),
                null,
                0,
                "Vendor"
        );
        doNothing().when(serviceUtil).checkUserAuthorization(any());
        when(serviceUtil.getUserFinance()).thenReturn(userFinance);
        when(receiptRepository.save(any())).thenReturn(receipt);
        String uuid = receiptService.addReceipt(userFinance.getUserUuid(), (ReceiptDto) receipt.transform());

        assert uuid.equals(receipt.getUuid());
    }

    @Test
    void updateReceipt() {
        UserFinance userFinance = UserFinance
                .builder()
                .id(1)
                .userUuid(UUID.randomUUID().toString())
                .username("username")
                .email("email@email.com")
                .name("John Doe")
                .isActive(true)
                .financialItems(new ArrayList<>())
                .build();
        Receipt receipt = new Receipt(
                1,
                UUID.randomUUID().toString(),
                userFinance,
                LocalDate.now(),
                1000.00,
                "Description",
                LocalDateTime.now(),
                null,
                0,
                "Vendor"
        );
        ReceiptDto dto = new ReceiptDto(
                receipt.getUuid(),
                LocalDate.now().plusDays(1),
                1.00,
                "N/A",
                "Another Vendor"
        );
        doNothing().when(serviceUtil).checkUserAuthorization(any());
        when(receiptRepository.findByUuid(any())).thenReturn(Optional.of(receipt));
        when(receiptRepository.save(any())).thenReturn(any());
        receiptService.updateReceipt(userFinance.getUserUuid(), dto);
        assert receipt.getUuid().equals(dto.getUuid());
        assert receipt.getVendor().equals(dto.getVendor());
        assert receipt.getDescription().equals(dto.getDescription());
        assert receipt.getAmount() == dto.getAmount();
        assert receipt.getDueDate().equals(dto.getDueDate());
    }

    @Test
    void deleteReceipt() {
        UserFinance userFinance = UserFinance
                .builder()
                .id(1)
                .userUuid(UUID.randomUUID().toString())
                .username("username")
                .email("email@email.com")
                .name("John Doe")
                .isActive(true)
                .financialItems(new ArrayList<>())
                .build();
        Receipt receipt = new Receipt(
                1,
                UUID.randomUUID().toString(),
                userFinance,
                LocalDate.now(),
                1000.00,
                "Description",
                LocalDateTime.now(),
                null,
                0,
                "Vendor"
        );
        doNothing().when(serviceUtil).checkUserAuthorization(any());
        when(receiptRepository.findByUuid(any())).thenReturn(Optional.of(receipt));
        doNothing().when(receiptRepository).delete(any());
    }
}