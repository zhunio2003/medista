package ec.insti.medista.security.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
public record RefreshTokenResponse (

    String refreshToken,
    String accessToken

){}
