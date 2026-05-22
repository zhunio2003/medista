package ec.insti.medista.security.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenResponse {

    private String refreshToken;
    private String accessToken;

}
