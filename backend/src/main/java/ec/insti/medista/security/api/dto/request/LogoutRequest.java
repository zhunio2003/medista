package ec.insti.medista.security.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public record LogoutRequest (

    @NotBlank
    String accessToken,

    @NotBlank
    String refreshToken

) {}
