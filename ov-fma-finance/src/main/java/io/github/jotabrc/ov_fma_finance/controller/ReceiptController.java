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

@RequestMapping(PREFIX + VERSION + "/finance/user/{userUuid}/receipt")
@RestController
public class ReceiptController {

    private final ReceiptService receiptService;
    private final RecurringReceiptService recurringReceiptService;

    public ReceiptController(ReceiptService receiptService, RecurringReceiptService recurringReceiptService) {
        this.receiptService = receiptService;
        this.recurringReceiptService = recurringReceiptService;
    }

    @PostMapping
    @Tag(name = "Receipt", description = "Add new Receipt")
    public ResponseEntity<String> addReceipt(@PathVariable final String userUuid, @RequestBody final ReceiptDto dto) {
        String uuid = receiptService.addReceipt(userUuid, dto);
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

    @PutMapping("/update")
    @Tag(name = "Update Receipt", description = "Updated Receipt")
    public ResponseEntity<String> updateReceipt(@PathVariable final String userUuid, @RequestBody ReceiptDto dto) {
        receiptService.updateReceipt(userUuid, dto);
        return ResponseEntity.ok("Receipt with UUID %s has been updated".formatted(dto.getUuid()));
    }

    @DeleteMapping("/{uuid}")
    @Tag(name = "Delete Receipt", description = "Delete Receipt")
    public ResponseEntity<String> deleteReceipt(@PathVariable final String userUuid, @PathVariable final String uuid) {
        receiptService.deleteReceipt(userUuid, uuid);
        return ResponseEntity.ok("Receipt with UUID %s has been deleted".formatted(uuid));
    }

    @PostMapping("/recurring")
    @Tag(name = "Recurring Receipt", description = "Add new Recurring Receipt")
    public ResponseEntity<String> addRecurringReceipt(@PathVariable final String userUuid, @RequestBody final RecurringReceiptDto dto) {
        String uuid = recurringReceiptService.addRecurringReceipt(userUuid, dto);
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

    @PutMapping("/recurring/update")
    @Tag(name = "Update Recurring Receipt", description = "Update Recurring Receipt")
    public ResponseEntity<String> updateRecurringReceipt(@PathVariable final String userUuid, @RequestBody RecurringReceiptDto dto) {
        recurringReceiptService.updateRecurringReceipt(userUuid, dto);
        return ResponseEntity.ok("Recurring Receipt with UUID %s has been updated".formatted(dto.getUuid()));
    }

    @DeleteMapping("/recurring/{uuid}")
    @Tag(name = "Delete Recurring Receipt", description = "Delete Recurring Receipt")
    public ResponseEntity<String> deleteRecurringReceipt(@PathVariable final String userUuid, @PathVariable final String uuid) {
        recurringReceiptService.deleteRecurringReceipt(userUuid, uuid);
        return  ResponseEntity.ok("Recurring Receipt with UUID %s has been deleted".formatted(uuid));
    }
}
