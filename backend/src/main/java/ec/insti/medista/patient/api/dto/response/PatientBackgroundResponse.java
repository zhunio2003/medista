package ec.insti.medista.patient.api.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class PatientBackgroundResponse {

    private String allergies;
    private String clinical;
    private String gynecological;
    private String traumatological;
    private String surgical;
    private String pharmacological;
    private OffsetDateTime updatedAt;

}
