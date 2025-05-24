package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.UserFinanceDto;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import io.github.jotabrc.ov_fma_finance.util.ServiceUtilImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class FinanceServiceImplTest {

    @Mock
    FinanceRepository financeRepository;

    @Mock
    ServiceUtilImpl serviceUtil;

    @InjectMocks
    FinanceServiceImpl financeService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addUserFinance() {
        UserFinanceDto dto = UserFinanceDto
                .builder()
                .userUuid(UUID.randomUUID().toString())
                .username("username")
                .email("email@email.com")
                .name("John Doe")
                .isActive(true)
                .financialItems(new ArrayList<>())
                .build();
        UserFinance userFinance = UserFinance
                .builder()
                .id(1)
                .userUuid(dto.getUserUuid())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .name(dto.getName())
                .isActive(dto.isActive())
                .financialItems(new ArrayList<>())
                .build();
        when(financeRepository.existsByUserUuid(any())).thenReturn(false);
        when(financeRepository.save(any())).thenReturn(userFinance);

        financeService.addUserFinance(dto);
        assert dto.getUserUuid().equals(userFinance.getUserUuid());
    }

    @Test
    void updateUserFinance() {
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
        UserFinanceDto dto = UserFinanceDto
                .builder()
                .userUuid(userFinance.getUserUuid())
                .username("newUsername")
                .email("new-email@email.com")
                .name("John Bloggs")
                .isActive(true)
                .financialItems(new ArrayList<>())
                .build();
        when(serviceUtil.getUserFinance()).thenReturn(userFinance);
        when(financeRepository.save(any())).thenReturn(userFinance);
        financeService.updateUserFinance(dto);
        assert
                userFinance.getUserUuid().equals(dto.getUserUuid()) &&
                        userFinance.getUsername().equals(dto.getUsername()) &&
                        userFinance.getEmail().equals(dto.getEmail()) &&
                        userFinance.getName().equals(dto.getName());
    }

    @Test
    void get() {
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
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserFinance> page = new PageImpl<>(List.of(userFinance), pageable, 0);
        doNothing().when(serviceUtil).checkUserAuthorization(any());
        when(financeRepository.findByDueDate(any(), any(), any(), any())).thenReturn(page);
        Page<UserFinanceDto> result = financeService.get(
                userFinance.getUserUuid(),
                LocalDate.of(2015, 10, 1),
                LocalDate.of(2015, 11, 1),
                0, 10);

        assert !result.isEmpty();
        assert result.get().allMatch(userFinanceDto -> userFinanceDto.getUserUuid().equals(userFinance.getUserUuid()));
    }
}