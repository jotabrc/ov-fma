package io.github.jotabrc.ov_fma_auth.controller;

import io.github.jotabrc.ov_fma_auth.dto.SignInDto;
import io.github.jotabrc.ov_fma_auth.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

import static io.github.jotabrc.ov_fma_auth.controller.ControllerPath.PREFIX;
import static io.github.jotabrc.ov_fma_auth.controller.ControllerPath.VERSION;

@RequestMapping(PREFIX + VERSION + "/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    @Tag(name = "SignIn Authentication", description = "Authenticates user")
    public ResponseEntity<String> signIn(@RequestBody final SignInDto dto) throws NoSuchAlgorithmException {
        String jwt = authService.signIn(dto);
        return ResponseEntity.ok("Token created: %s".formatted(jwt));
    }
}
