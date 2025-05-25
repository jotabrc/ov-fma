package io.github.jotabrc.ov_fma_auth.controller;

import io.github.jotabrc.ov_fma_auth.dto.LoginDto;
import io.github.jotabrc.ov_fma_auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

import static io.github.jotabrc.ov_fma_auth.controller.ControllerPath.PREFIX;
import static io.github.jotabrc.ov_fma_auth.controller.ControllerPath.VERSION;

@RequestMapping(PREFIX + VERSION + "/auth")
@RestController
public final class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/{userUuid}")
    @Tag(name = "User login", description = "Authenticates user")
    public ResponseEntity<String> login(@PathVariable("userUuid") final String userUuid, @RequestBody final LoginDto dto) throws NoSuchAlgorithmException {
        String jwt = authService.login(userUuid, dto);
        return ResponseEntity.ok("""
                        {
                            "message": "Token created",
                            "token": "%s"
                        }
                        """.formatted(jwt));
    }
}
