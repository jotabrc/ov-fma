package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.RecurringPaymentDto;
import io.github.jotabrc.ov_fma_finance.model.RecurringPayment;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.RecurringPaymentRepository;
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

class RecurringPaymentServiceImplTest {

    @Mock
    private RecurringPaymentRepository recurringPaymentRepository;

    @Mock
    private ServiceUtilImpl serviceUtil;

    @InjectMocks
    private RecurringPaymentServiceImpl recurringPaymentService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addRecurringPayment() {
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
        RecurringPayment recurringPayment = new RecurringPayment(
                1,
                UUID.randomUUID().toString(),
                userFinance,
                LocalDate.now(),
                120.23,
                "Description",
                LocalDateTime.now(),
                null,
                0,
                LocalDate.now().plusMonths(3),
                "Payee"
        );
        doNothing().when(serviceUtil).checkUserAuthorization(any());
        when(serviceUtil.getUserFinance()).thenReturn(userFinance);
        when(recurringPaymentRepository.save(any())).thenReturn(recurringPayment);
        String uuid = recurringPaymentService.addRecurringPayment(userFinance.getUserUuid(), (RecurringPaymentDto) recurringPayment.transform());
        assert uuid.equals(recurringPayment.getUuid());
    }

    @Test
    void updateRecurringPayment() {
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
        RecurringPayment recurringPayment = new RecurringPayment(
                1,
                UUID.randomUUID().toString(),
                userFinance,
                LocalDate.now(),
                120.23,
                "Description",
                LocalDateTime.now(),
                null,
                0,
                LocalDate.now().plusMonths(3),
                "Payee"
        );
        RecurringPaymentDto dto = new RecurringPaymentDto(
                recurringPayment.getUuid(),
                LocalDate.now().plusDays(5),
                129.36,
                "New Description",
                LocalDate.now().plusMonths(2),
                "New Payee"
        );
        doNothing().when(serviceUtil).checkUserAuthorization(any());
        when(recurringPaymentRepository.findByUuid(any())).thenReturn(Optional.of(recurringPayment));
        when(recurringPaymentRepository.save(any())).thenReturn(any());
        recurringPaymentService.updateRecurringPayment(userFinance.getUserUuid(), dto);
        assert recurringPayment.getPayee().equals(dto.getPayee());
        assert recurringPayment.getRecurringUntil().equals(dto.getRecurringUntil());
        assert recurringPayment.getDescription().equals(dto.getDescription());
        assert recurringPayment.getAmount() == dto.getAmount();
        assert recurringPayment.getDueDate().equals(dto.getDueDate());
        assert recurringPayment.getUuid().equals(dto.getUuid());
    }

    @Test
    void deleteRecurringPayment() {
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
        RecurringPayment recurringPayment = new RecurringPayment(
                1,
                UUID.randomUUID().toString(),
                userFinance,
                LocalDate.now(),
                120.23,
                "Description",
                LocalDateTime.now(),
                null,
                0,
                LocalDate.now().plusMonths(3),
                "Payee"
        );
        doNothing().when(serviceUtil).checkUserAuthorization(any());
        when(recurringPaymentRepository.findByUuid(any())).thenReturn(Optional.of(recurringPayment));
        doNothing().when(recurringPaymentRepository).delete(any());
        recurringPaymentService.deleteRecurringPayment(userFinance.getUserUuid(), recurringPayment.getUuid());
    }
}