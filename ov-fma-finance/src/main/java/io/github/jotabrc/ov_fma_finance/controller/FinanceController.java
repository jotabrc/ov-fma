package io.github.jotabrc.ov_fma_finance.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.github.jotabrc.ov_fma_finance.controller.ControllerPath.PREFIX;
import static io.github.jotabrc.ov_fma_finance.controller.ControllerPath.VERSION;

@RequestMapping(PREFIX + VERSION + "/finance")
@RestController
public class FinanceController {

}
