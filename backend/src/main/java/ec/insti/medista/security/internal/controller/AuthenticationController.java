package ec.insti.medista.security.internal.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.insti.medista.security.api.AuthenticationService;
import ec.insti.medista.security.api.dto.request.LoginRequest;
import ec.insti.medista.security.api.dto.request.LogoutRequest;
import ec.insti.medista.security.api.dto.request.RefreshTokenRequest;
import ec.insti.medista.security.api.dto.response.LoginResponse;
import ec.insti.medista.security.api.dto.response.RefreshTokenResponse;
import ec.insti.medista.security.internal.service.impl.ratelimit.LoginRateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final LoginRateLimiter loginRateLimiter;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        String ip = httpRequest.getRemoteAddr();

        if (!loginRateLimiter.isAllowed(ip)) {
            return ResponseEntity.status(429).build();
        }

        LoginResponse response = authenticationService.login(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        authenticationService.logout(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        
        RefreshTokenResponse response = authenticationService.refresh(request);        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
