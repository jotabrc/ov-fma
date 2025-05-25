package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.PaymentDto;
import io.github.jotabrc.ov_fma_finance.model.Payment;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.PaymentRepository;
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

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ServiceUtilImpl serviceUtil;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPayment() {
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
        Payment payment = new Payment(1,
                UUID.randomUUID().toString(),
                userFinance,
                LocalDate.now(),
                100.00,
                "Test",
                LocalDateTime.now(),
                null,
                0,
                "Payee"
                );
        PaymentDto paymentDto = (PaymentDto) payment.transform();
        doNothing().when(serviceUtil).checkUserAuthorization(any());
        when(serviceUtil.getUserFinance()).thenReturn(userFinance);
        when(paymentRepository.save(any())).thenReturn(payment);
        String uuid = paymentService.addPayment(userFinance.getUserUuid(), paymentDto);
        assert uuid.equals(payment.getUuid());
    }

    @Test
    void updatePayment() {
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
        Payment payment = new Payment(1,
                UUID.randomUUID().toString(),
                userFinance,
                LocalDate.now(),
                100.00,
                "Test",
                LocalDateTime.now(),
                null,
                0,
                "Payee"
        );
        PaymentDto dto = new PaymentDto(
                UUID.randomUUID().toString(),
                LocalDate.now(),
                150.00,
                "Updated",
                "Another Payee"
        );
        doNothing().when(serviceUtil).checkUserAuthorization(any());
        when(paymentRepository.findByUuid(any())).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any())).thenReturn(any());
        paymentService.updatePayment(userFinance.getUserUuid(), dto);

        assert payment.getAmount() == dto.getAmount() &&
                payment.getDescription().equals(dto.getDescription()) &&
                payment.getPayee().equals(dto.getPayee());
    }

    @Test
    void deletePayment() {
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
        Payment payment = new Payment(1,
                UUID.randomUUID().toString(),
                userFinance,
                LocalDate.now(),
                100.00,
                "Test",
                LocalDateTime.now(),
                null,
                0,
                "Payee"
        );
        doNothing().when(serviceUtil).checkUserAuthorization(any());
        when(paymentRepository.findByUuid(any())).thenReturn(Optional.of(payment));
        doNothing().when(paymentRepository).delete(any());
    }
}