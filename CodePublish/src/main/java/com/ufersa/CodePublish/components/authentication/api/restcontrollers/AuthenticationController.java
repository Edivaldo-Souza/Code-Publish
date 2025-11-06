package com.ufersa.CodePublish.components.authentication.api.restcontrollers;

import com.ufersa.CodePublish.components.authentication.api.dtos.LoginDto;
import com.ufersa.CodePublish.components.authentication.api.dtos.TokenDto;
import com.ufersa.CodePublish.components.authentication.domain.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signin(@RequestBody LoginDto loginDto) {
        TokenDto token = authenticationService.signin(loginDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signin-cookie")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
        // Lógica de autenticação do usuário
        TokenDto token = authenticationService.signin(loginDto);

        ResponseCookie cookie = ResponseCookie.from("accessToken", token.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(28800)
                .domain("secure-tiffanie-myproject0211-cfa923f1.koyeb.app/")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = ResponseCookie.from("accessToken", null)
                .path("/")
                .maxAge(0)
                .domain("secure-tiffanie-myproject0211-cfa923f1.koyeb.app/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Você foi deslogado com sucesso!");
    }
}
