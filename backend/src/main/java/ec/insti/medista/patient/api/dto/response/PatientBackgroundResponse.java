package ec.insti.medista.patient.api.dto.response;

import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record PatientBackgroundResponse(
        String allergies,
        String clinical,
        String gynecological,
        String traumatological,
        String surgical,
        String pharmacological,
        OffsetDateTime updatedAt
) {}