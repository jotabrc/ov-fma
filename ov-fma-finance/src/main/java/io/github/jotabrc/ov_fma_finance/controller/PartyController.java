package io.github.jotabrc.ov_fma_finance.controller;


import io.github.jotabrc.ov_fma_finance.dto.PartyDto;
import io.github.jotabrc.ov_fma_finance.service.PartyService;
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

@RequestMapping(PREFIX + VERSION + "/party")
@RestController
public class PartyController {

    private final PartyService partyService;

    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @PostMapping("/add-party")
    @Tag(name = "Party", description = "Add new Party")
    public ResponseEntity<String> addParty(@RequestBody final PartyDto dto) {
        String uuid = partyService.addParty(dto);
        // Creates location path
        URI location = ServletUriComponentsBuilder
                .fromPath(PREFIX + VERSION + "/party/get-by-uuid/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        ControllerMessage controllerMessage = ControllerMessage
                .builder()
                .message("Party created")
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
