package ec.insti.medista.security.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
public record LoginResponse (

    String accessToken,
    String refreshToken

){}
