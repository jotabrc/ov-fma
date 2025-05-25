package io.github.jotabrc.ov_fma_finance.service;

import io.github.jotabrc.ov_fma_finance.dto.UserFinanceDto;
import io.github.jotabrc.ov_fma_finance.model.UserFinance;
import io.github.jotabrc.ov_fma_finance.repository.FinanceRepository;
import io.github.jotabrc.ov_fma_finance.service.util.ServiceUtilImpl;
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
    private FinanceRepository financeRepository;

    @Mock
    private ServiceUtilImpl serviceUtil;

    @InjectMocks
    private FinanceServiceImpl financeService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addUserFinance() {
        UserFinanceDto dto = UserFinanceDto
                .builder()
                .userUuid(UUID.randomUUID().toString())
                .name("John Doe")
                .financialItems(new ArrayList<>())
                .build();
        UserFinance userFinance = UserFinance
                .builder()
                .id(1)
                .userUuid(dto.getUserUuid())
                .name(dto.getName())
                .financialItems(new ArrayList<>())
                .build();
        when(financeRepository.existsByUserUuid(any())).thenReturn(false);
        when(financeRepository.save(any())).thenReturn(userFinance);

        financeService.save(dto);
        assert dto.getUserUuid().equals(userFinance.getUserUuid());
    }

    @Test
    void updateUserFinance() {
        UserFinance userFinance = UserFinance
                .builder()
                .id(1)
                .userUuid(UUID.randomUUID().toString())
                .name("John Doe")
                .financialItems(new ArrayList<>())
                .build();
        UserFinanceDto dto = UserFinanceDto
                .builder()
                .userUuid(userFinance.getUserUuid())
                .name("John Bloggs")
                .financialItems(new ArrayList<>())
                .build();
        when(serviceUtil.getUserFinance()).thenReturn(userFinance);
        when(financeRepository.save(any())).thenReturn(userFinance);
        financeService.update(dto);
        assert
                userFinance.getUserUuid().equals(dto.getUserUuid()) &&
                        userFinance.getName().equals(dto.getName());
    }

    @Test
    void get() {
        UserFinance userFinance = UserFinance
                .builder()
                .id(1)
                .userUuid(UUID.randomUUID().toString())
                .name("John Doe")
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