package io.github.jotabrc.ov_fma_finance.controller;

import io.github.jotabrc.ov_fma_finance.dto.ReceiptDto;
import io.github.jotabrc.ov_fma_finance.dto.RecurringReceiptDto;
import io.github.jotabrc.ov_fma_finance.service.ReceiptService;
import io.github.jotabrc.ov_fma_finance.service.RecurringReceiptService;
import io.github.jotabrc.ov_fma_finance.util.ControllerMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
                .message("Receipt has been created")
                .uuid(uuid)
                .build();
        return  ResponseEntity
                .created(location)
                .body(
                        controllerMessage
                                .toJSON()
                );
    }

    @PutMapping("/update-receipt")
    @Tag(name = "Update Receipt", description = "Updated Receipt")
    public ResponseEntity<String> updateReceipt(@RequestBody ReceiptDto dto) {
        receiptService.updateReceipt(dto);
        return ResponseEntity.ok("Receipt with UUID %s has been updated".formatted(dto.getUuid()));
    }

    @DeleteMapping("/delete-receipt/{uuid}")
    @Tag(name = "Delete Receipt", description = "Delete Receipt")
    public ResponseEntity<String> deleteReceipt(@PathVariable final String uuid) {
        receiptService.deleteReceipt(uuid);
        return ResponseEntity.ok("Receipt with UUID %s has been deleted".formatted(uuid));
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
                .message("Recurring Receipt has been created")
                .uuid(uuid)
                .build();
        return  ResponseEntity
                .created(location)
                .body(
                        controllerMessage
                                .toJSON()
                );
    }

    @PutMapping("/update-recurring-receipt")
    @Tag(name = "Update Recurring Receipt", description = "Update Recurring Receipt")
    public ResponseEntity<String> updateRecurringReceipt(@RequestBody RecurringReceiptDto dto) {
        recurringReceiptService.updateRecurringReceipt(dto);
        return ResponseEntity.ok("Recurring Receipt with UUID %s has been updated".formatted(dto.getUuid()));
    }

    @DeleteMapping("/delete-recurring-receipt/{uuid}")
    @Tag(name = "Delete Recurring Receipt", description = "Delete Recurring Receipt")
    public ResponseEntity<String> deleteRecurringReceipt(@PathVariable final String uuid) {
        recurringReceiptService.deleteRecurringReceipt(uuid);
        return  ResponseEntity.ok("Recurring Receipt with UUID %s has been deleted".formatted(uuid));
    }
}
