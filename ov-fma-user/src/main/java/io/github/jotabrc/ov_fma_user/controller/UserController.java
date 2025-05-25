package io.github.jotabrc.ov_fma_user.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.jotabrc.ov_fma_user.dto.UserCreationUpdateDto;
import io.github.jotabrc.ov_fma_user.dto.UserDto;
import io.github.jotabrc.ov_fma_user.service.UserService;
import io.github.jotabrc.ov_fma_user.util.ControllerMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.NoSuchAlgorithmException;

import static io.github.jotabrc.ov_fma_user.controller.ControllerPath.PREFIX;
import static io.github.jotabrc.ov_fma_user.controller.ControllerPath.VERSION;

@RequestMapping(PREFIX + VERSION + "/user")
@RestController
public final class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Tag(name = "User SIGNUP", description = "Register's new user")
    public ResponseEntity<String> save(@RequestBody final UserCreationUpdateDto dto) throws NoSuchAlgorithmException, JsonProcessingException {
        String uuid = userService.save(dto).getUuid();
        // Creates location path to retrieve user information
        URI location = ServletUriComponentsBuilder
                .fromPath(PREFIX + VERSION + "/user/get/{uuid}")
                .buildAndExpand(uuid)
                .toUri();
        ControllerMessage controllerMessage = ControllerMessage
                .builder()
                .message("User signup was successful")
                .uuid(uuid)
                .build();
        return ResponseEntity
                .created(location)
                .body(
                        controllerMessage
                                .toJSON()
                );
    }

    @PutMapping("/update/{userUuid}")
    @Tag(name = "User UPDATE", description = "Updates user information, requires valid UUID")
    public ResponseEntity<String> update(@PathVariable("userUuid") final String userUuid, @RequestBody final UserCreationUpdateDto dto) throws NoSuchAlgorithmException, JsonProcessingException {
        userService.update(userUuid, dto);
        ControllerMessage controllerMessage = ControllerMessage
                .builder()
                .message("User update was successful")
                .uuid(userUuid)
                .build();
        return ResponseEntity
                .ok(
                        controllerMessage
                                .toJSON()
                );
    }

    @GetMapping("/get/{userUuid}")
    @Tag(name = "User GET BY UUID", description = "Get user information with uuid")
    public ResponseEntity<UserDto> get(@PathVariable("userUuid") final String userUuid) {
        UserDto dto = userService.get(userUuid);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .body(dto);
    }
}
