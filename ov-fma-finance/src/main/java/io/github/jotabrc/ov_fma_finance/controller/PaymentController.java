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

@RequestMapping(PREFIX + VERSION + "/finance/payment")
@RestController
public class PaymentController {

    private final PaymentService paymentService;
    private final RecurringPaymentService recurringPaymentService;

    public PaymentController(PaymentService paymentService, RecurringPaymentService recurringPaymentService) {
        this.paymentService = paymentService;
        this.recurringPaymentService = recurringPaymentService;
    }

    @PostMapping("/add-payment")
    @Tag(name = "Payment", description = "Add new Payment")
    public ResponseEntity<String> addPayment(@RequestBody final PaymentDto dto) {
        String uuid = paymentService.addPayment(dto);
        // Creates location path
        URI location = ServletUriComponentsBuilder
                .fromPath(PREFIX + VERSION + "/payment/get-by-uuid/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        ControllerMessage controllerMessage = ControllerMessage
                .builder()
                .message("Payment created")
                .uuid(uuid)
                .build();
        return  ResponseEntity
                .created(location)
                .body(
                        controllerMessage
                                .toJSON()
                );
    }

    @PutMapping("/update-payment")
    @Tag(name = "Update Payment", description = "Update Payment")
    public ResponseEntity<String> updatePayment(@RequestBody final PaymentDto dto) {
        paymentService.updatePayment(dto);
        return  ResponseEntity.ok("Payment with ID %d has been updated".formatted(dto.getId()));
    }

    @DeleteMapping("/delete-payment/{id}")
    @Tag(name = "Delete Payment", description = "Delete Payment")
    public ResponseEntity<String> deletePayment(@PathVariable final long id) {
        paymentService.deletePayment(id);
        return  ResponseEntity.ok("Payment with ID %d has been deleted".formatted(id));
    }

    @PostMapping("/add-recurring-payment")
    @Tag(name = "Recurring Payment", description = "Add new Recurring Payment")
    public ResponseEntity<String> addRecurringPayment(@RequestBody final RecurringPaymentDto dto) {
        String uuid = recurringPaymentService.addRecurringPayment(dto);
        // Creates location path
        URI location = ServletUriComponentsBuilder
                .fromPath(PREFIX + VERSION + "/recurring-payment/get-by-uuid/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        ControllerMessage controllerMessage = ControllerMessage
                .builder()
                .message("Recurring Payment created")
                .uuid(uuid)
                .build();
        return  ResponseEntity
                .created(location)
                .body(
                        controllerMessage
                                .toJSON()
                );
    }

    @PutMapping("/update-recurring-payment")
    @Tag(name = "Update Recurring Payment", description = "Update Recurring Payment")
    public ResponseEntity<String> updateRecurringPayment(@RequestBody final RecurringPaymentDto dto) {
        recurringPaymentService.updateRecurringPayment(dto);
        return  ResponseEntity.ok("Recurring Payment with ID %d has been updated".formatted(dto.getId()));
    }

    @DeleteMapping("/delete-recurring-payment/{id}")
    @Tag(name = "Delete Recurring Payment", description = "Delete Recurring Payment")
    public ResponseEntity<String> deleteRecurringPayment(@PathVariable final long id) {
        recurringPaymentService.deleteRecurringPayment(id);
        return  ResponseEntity.ok("Recurring Payment with ID %d has been deleted".formatted(id));
    }
}
