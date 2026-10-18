package ec.insti.medista.patient.api.dto.response;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record PatientResponse(
        Long id,
        Long userId,
        String cedula,
        String firstName,
        String lastName,
        Long careerId,
        String careerName,
        String semester,
        String address,
        String neighborhood,
        String parish,
        String canton,
        String province,
        String phone,
        LocalDate birthDate,
        int age,
        String birthPlace,
        String birthCountry,
        String gender,
        String maritalStatus,
        String bloodType,
        Boolean isActive,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}