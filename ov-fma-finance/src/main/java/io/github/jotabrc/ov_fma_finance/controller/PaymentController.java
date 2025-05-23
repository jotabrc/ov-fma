package io.github.jotabrc.ov_fma_finance.controller;


import io.github.jotabrc.ov_fma_finance.dto.PaymentDto;
import io.github.jotabrc.ov_fma_finance.dto.RecurringPaymentDto;
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

@RequestMapping(PREFIX + VERSION + "/user/{userUuid}/payment")
@RestController
public class PaymentController {

    private final PaymentService paymentService;
    private final RecurringPaymentService recurringPaymentService;

    public PaymentController(PaymentService paymentService, RecurringPaymentService recurringPaymentService) {
        this.paymentService = paymentService;
        this.recurringPaymentService = recurringPaymentService;
    }

    @PostMapping
    @Tag(name = "Payment", description = "Add new Payment")
    public ResponseEntity<String> addPayment(@PathVariable final String userUuid, @RequestBody final PaymentDto dto) {
        String uuid = paymentService.addPayment(userUuid, dto);
        // Creates location path
        URI location = ServletUriComponentsBuilder
                .fromPath(PREFIX + VERSION + "/payment/get-by-uuid/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        ControllerMessage controllerMessage = ControllerMessage
                .builder()
                .message("Payment has been created")
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
    @Tag(name = "Update Payment", description = "Update Payment")
    public ResponseEntity<String> updatePayment(@PathVariable final String userUuid, @RequestBody final PaymentDto dto) {
        paymentService.updatePayment(userUuid, dto);
        return  ResponseEntity.ok("Payment with UUID %s has been updated".formatted(dto.getUuid()));
    }

    @DeleteMapping("/{uuid}")
    @Tag(name = "Delete Payment", description = "Delete Payment")
    public ResponseEntity<String> deletePayment(@PathVariable final String userUuid, @PathVariable final String uuid) {
        paymentService.deletePayment(userUuid, uuid);
        return  ResponseEntity.ok("Payment with UUID %s has been deleted".formatted(uuid));
    }

    @PostMapping("/recurring")
    @Tag(name = "Recurring Payment", description = "Add new Recurring Payment")
    public ResponseEntity<String> addRecurringPayment(@PathVariable final String userUuid, @RequestBody final RecurringPaymentDto dto) {
        String uuid = recurringPaymentService.addRecurringPayment(userUuid, dto);
        // Creates location path
        URI location = ServletUriComponentsBuilder
                .fromPath(PREFIX + VERSION + "/recurring-payment/get-by-uuid/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        ControllerMessage controllerMessage = ControllerMessage
                .builder()
                .message("Recurring Payment has been created")
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
    @Tag(name = "Update Recurring Payment", description = "Update Recurring Payment")
    public ResponseEntity<String> updateRecurringPayment(@PathVariable final String userUuid, @RequestBody final RecurringPaymentDto dto) {
        recurringPaymentService.updateRecurringPayment(userUuid, dto);
        return  ResponseEntity.ok("Recurring Payment with UUID %s has been updated".formatted(dto.getUuid()));
    }

    @DeleteMapping("/recurring/{uuid}")
    @Tag(name = "Delete Recurring Payment", description = "Delete Recurring Payment")
    public ResponseEntity<String> deleteRecurringPayment(@PathVariable final String userUuid, @PathVariable final String uuid) {
        recurringPaymentService.deleteRecurringPayment(userUuid, uuid);
        return  ResponseEntity.ok("Recurring Payment with UUID %s has been deleted".formatted(uuid));
    }
}
