package io.github.jotabrc.ov_fma_finance.controller;

import io.github.jotabrc.ov_fma_finance.dto.ReceiptDto;
import io.github.jotabrc.ov_fma_finance.dto.RecurringReceiptDto;
import io.github.jotabrc.ov_fma_finance.service.ReceiptService;
import io.github.jotabrc.ov_fma_finance.service.RecurringReceiptService;
import io.github.jotabrc.ov_fma_finance.util.ControllerMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static io.github.jotabrc.ov_fma_finance.controller.ControllerPath.PREFIX;
import static io.github.jotabrc.ov_fma_finance.controller.ControllerPath.VERSION;

@RequestMapping(PREFIX + VERSION + "/finance/receipt")
@RestController
public class ReceiptController {

    private final ReceiptService receiptService;
    private final RecurringReceiptService recurringReceiptService;

    public ReceiptController(ReceiptService receiptService, RecurringReceiptService recurringReceiptService) {
        this.receiptService = receiptService;
        this.recurringReceiptService = recurringReceiptService;
    }

    @PostMapping("/add-receipt")
    @Tag(name = "Receipt", description = "Add new Receipt")
    public ResponseEntity<String> addReceipt(@RequestBody final ReceiptDto dto) {
        String uuid = receiptService.addReceipt(dto);
        // Creates location path
        URI location = ServletUriComponentsBuilder
                .fromPath(PREFIX + VERSION + "/receipt/get-by-uuid/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        ControllerMessage controllerMessage = ControllerMessage
                .builder()
                .message("Receipt created")
                .uuid(uuid)
                .build();
        return  ResponseEntity
                .created(location)
                .body(
                        controllerMessage
                                .toJSON()
                );
    }

    @PostMapping("/add-recurring-receipt")
    @Tag(name = "Recurring Receipt", description = "Add new Recurring Receipt")
    public ResponseEntity<String> addRecurringReceipt(@RequestBody final RecurringReceiptDto dto) {
        String uuid = recurringReceiptService.addRecurringReceipt(dto);
        // Creates location path
        URI location = ServletUriComponentsBuilder
                .fromPath(PREFIX + VERSION + "/recurring-receipt/get-by-uuid/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        ControllerMessage controllerMessage = ControllerMessage
                .builder()
                .message("Recurring Receipt created")
                .uuid(uuid)
                .build();
        return  ResponseEntity
                .created(location)
                .body(
                        controllerMessage
                                .toJSON()
                );
    }
}
