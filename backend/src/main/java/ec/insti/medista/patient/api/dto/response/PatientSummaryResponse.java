package ec.insti.medista.patient.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PatientSummaryResponse {

    private Long id;
    private String cedula;
    private String firstName;
    private String lastName;
    private String careerName;
    private String semester;
    private Boolean isActive;

}
