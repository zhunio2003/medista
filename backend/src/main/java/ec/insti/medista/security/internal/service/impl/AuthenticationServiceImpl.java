package ec.insti.medista.security.internal.service.impl;

import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ec.insti.medista.security.api.AuthenticationService;
import ec.insti.medista.security.api.dto.request.LoginRequest;
import ec.insti.medista.security.api.dto.request.LogoutRequest;
import ec.insti.medista.security.api.dto.request.RefreshTokenRequest;
import ec.insti.medista.security.api.dto.response.LoginResponse;
import ec.insti.medista.security.api.dto.response.RefreshTokenResponse;
import ec.insti.medista.security.internal.entity.User;
import ec.insti.medista.security.internal.repository.UserRepository;
import ec.insti.medista.security.internal.service.impl.jwt.JwtBlacklistService;
import ec.insti.medista.security.internal.service.impl.jwt.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService, UserDetailsService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtBlacklistService jwtBlackistService;
    private final PasswordEncoder passwordEncoder;

    // Username is email
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByEmail(username);
        
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Email no encontrado " + username);
        }
        return userOptional.get();

    }

    @Override
    public LoginResponse login(LoginRequest request) {

        UserDetails userEncontrado = loadUserByUsername(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(),userEncontrado.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(userEncontrado);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userEncontrado);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
            .build();

    }

    @Override
    public void logout(LogoutRequest request) {
        
        jwtBlackistService.blacklistToken(request.getAccessToken());
        jwtBlackistService.blacklistToken(request.getRefreshToken());
        
    }

    @Override
    public RefreshTokenResponse refresh(RefreshTokenRequest request) {
        
        if (!jwtTokenProvider.validateToken(request.getRefreshToken()) ||
            jwtBlackistService.isBlacklisted(request.getRefreshToken()) || 
            !jwtTokenProvider.extractClaim(request.getRefreshToken(), "type").equals("refresh")) {
            throw new JwtException("TOKEN INVALIDO");
        }

        String username = jwtTokenProvider.extractUsername(request.getRefreshToken());

        UserDetails userEncontrado = loadUserByUsername(username);

        String accessToken = jwtTokenProvider.generateAccessToken(userEncontrado);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userEncontrado);

        jwtBlackistService.blacklistToken(request.getRefreshToken());

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
            .build();
        
    }
    
}
