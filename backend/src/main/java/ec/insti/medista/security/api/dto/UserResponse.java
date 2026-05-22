package ec.insti.medista.security.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String role;

}
