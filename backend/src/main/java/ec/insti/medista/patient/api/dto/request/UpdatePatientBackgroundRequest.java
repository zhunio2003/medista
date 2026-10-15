package ec.insti.medista.patient.api.dto.request;

import jakarta.validation.constraints.*;
import java.time.OffsetDateTime;

public record UpdatePatientBackgroundRequest(

        @Size(max = 2000, message = "Las alergias no pueden superar 2000 caracteres")
        String allergies,

        @Size(max = 2000, message = "Los antecedentes clínicos no pueden superar 2000 caracteres")
        String clinical,

        @Size(max = 2000, message = "Los antecedentes ginecológicos no pueden superar 2000 caracteres")
        String gynecological,

        @Size(max = 2000, message = "Los antecedentes traumatológicos no pueden superar 2000 caracteres")
        String traumatological,

        @Size(max = 2000, message = "Los antecedentes quirúrgicos no pueden superar 2000 caracteres")
        String surgical,

        @Size(max = 2000, message = "Los antecedentes farmacológicos no pueden superar 2000 caracteres")
        String pharmacological

){}
