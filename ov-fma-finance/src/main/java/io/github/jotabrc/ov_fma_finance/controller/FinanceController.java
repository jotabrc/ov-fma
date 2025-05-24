package io.github.jotabrc.ov_fma_finance.controller;

import io.github.jotabrc.ov_fma_finance.dto.UserFinanceDto;
import io.github.jotabrc.ov_fma_finance.service.FinanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static io.github.jotabrc.ov_fma_finance.controller.ControllerPath.PREFIX;
import static io.github.jotabrc.ov_fma_finance.controller.ControllerPath.VERSION;

@RequestMapping(PREFIX + VERSION + "/finance/user/{userUuid}")
@RestController
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping("/get")
    @Tag(name = "Get Financial Data", description = "Requires User UUID, initial and final date")
    public ResponseEntity<Page<UserFinanceDto>> get(
            @RequestParam final String userUuid,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate toDate,
            @RequestParam final int pageStart,
            @RequestParam final int pageSize) {
        Page<UserFinanceDto> page = financeService.get(userUuid, fromDate, toDate, pageStart, pageSize);
        return ResponseEntity.ok(page);
    }
}
