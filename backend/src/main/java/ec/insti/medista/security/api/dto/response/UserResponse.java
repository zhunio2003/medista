package ec.insti.medista.security.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
public record UserResponse (

    String firstName,
    String lastName,
    String email,
    String role

){}
