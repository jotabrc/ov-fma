package io.github.jotabrc.ov_fma_finance.controller;


import io.github.jotabrc.ov_fma_finance.dto.PaymentDto;
import io.github.jotabrc.ov_fma_finance.dto.RecurringPaymentDto;
import io.github.jotabrc.ov_fma_finance.dto.UserFinanceDto;
import io.github.jotabrc.ov_fma_finance.service.FinanceService;
import io.github.jotabrc.ov_fma_finance.service.PaymentService;
import io.github.jotabrc.ov_fma_finance.service.RecurringPaymentService;
import io.github.jotabrc.ov_fma_finance.util.ControllerMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static io.github.jotabrc.ov_fma_finance.controller.ControllerPath.PREFIX;
import static io.github.jotabrc.ov_fma_finance.controller.ControllerPath.VERSION;

@RequestMapping(PREFIX + VERSION + "/finance/user")
@RestController
public class UserController {

    private final FinanceService financeService;

    public UserController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping("get/{uuid}")
    public ResponseEntity<UserFinanceDto> getByUserUuid(@PathVariable final String userUuid) {

    }
}
