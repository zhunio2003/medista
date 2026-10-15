package ec.insti.medista.patient.api.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UpdatePatientRequest(

    @NotNull(message = "Debes estudiar una carrera")
    @Positive
    Long careerId,

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[\\p{L}][\\p{L} '\\-]*$", message = "El nombre solo puede contener letras")
    @Size(min = 2, max = 100)
    String firstName,

    @NotBlank(message = "El apellido es obligatorio")
    @Pattern(regexp = "^[\\p{L}][\\p{L} '\\-]*$", message = "El apellido solo puede contener letras")
    @Size(min = 2, max = 100)
    String lastName,

    @NotBlank(message = "El ciclo es obligatorio")
    @Size(max = 20)
    String semester,

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255)
    String address,

    @Size(max = 100)
    String neighborhood,

    @Size(max = 100)
    String parish,

    @NotBlank(message = "El cantón es obligatorio")
    @Size(min = 2, max = 100)
    String canton,

    @NotBlank(message = "La provincia es obligatoria")
    @Size(min = 2, max = 100)
    String province,

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{7,20}", message = "El teléfono debe tener entre 7 y 20 dígitos")
    String phone,

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    LocalDate birthDate,

    @Size(max = 100)
    String birthPlace,

    @Size(max = 100)
    String birthCountry,

    @NotBlank(message = "El género es obligatorio")
    @Pattern(regexp = "MALE|FEMALE", message = "Género inválido")
    String gender,

    @NotBlank(message = "El estado civil es obligatorio")
    @Pattern(regexp = "SINGLE|MARRIED|DIVORCED|WIDOWED|FREE_UNION", message = "Estado civil inválido")
    String maritalStatus,

    @NotBlank(message = "El Tipo de sangre es obligatorio")
    @Pattern(regexp = "A_POSITIVE|A_NEGATIVE|B_POSITIVE|B_NEGATIVE|AB_POSITIVE|AB_NEGATIVE|O_POSITIVE|O_NEGATIVE", message = "Tipo de sangre inválido")
    String bloodType

){}
