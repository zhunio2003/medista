package ec.insti.medista.patient.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
public record PatientSummaryResponse (
    Long id,
    String cedula,
    String firstName,
    String lastName,
    String careerName,
    String semester,
    Boolean isActive
){};
