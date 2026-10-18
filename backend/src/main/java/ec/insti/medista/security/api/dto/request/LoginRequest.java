package ec.insti.medista.security.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;


public record LoginRequest (

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    String email,

    @NotBlank(message = "La contraseña es obligatoria")
    String password

) {}
