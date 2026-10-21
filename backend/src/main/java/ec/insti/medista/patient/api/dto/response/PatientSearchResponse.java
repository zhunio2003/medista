package ec.insti.medista.patient.api.dto.response;

public record PatientSearchResponse (

        Long id,
        String cedula,
        String fullName,
        String careerName,
        Double similarity

){}
