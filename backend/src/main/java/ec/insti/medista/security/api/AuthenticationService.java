package ec.insti.medista.security.api;

import ec.insti.medista.security.api.dto.request.LoginRequest;
import ec.insti.medista.security.api.dto.request.LogoutRequest;
import ec.insti.medista.security.api.dto.request.RefreshTokenRequest;
import ec.insti.medista.security.api.dto.response.LoginResponse;
import ec.insti.medista.security.api.dto.response.RefreshTokenResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request);
    void logout(LogoutRequest request);
    RefreshTokenResponse refresh(RefreshTokenRequest request);
    
    
} 
