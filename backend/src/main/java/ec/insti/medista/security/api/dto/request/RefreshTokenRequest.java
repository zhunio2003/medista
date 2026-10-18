package ec.insti.medista.security.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public record RefreshTokenRequest (

    @NotBlank
    String refreshToken

){}
