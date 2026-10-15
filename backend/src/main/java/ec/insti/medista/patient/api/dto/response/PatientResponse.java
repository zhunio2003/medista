package ec.insti.medista.patient.api.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Builder
public class PatientResponse {

    private Long id;
    private Long userId;
    private String cedula;
    private String firstName;
    private String lastName;
    private Long careerId;
    private String careerName;
    private String semester;
    private String address;
    private String neighborhood;
    private String parish;
    private String canton;
    private String province;
    private String phone;
    private LocalDate birthDate;
    private int age;
    private String birthPlace;
    private String birthCountry;
    private String gender;
    private String maritalStatus;
    private String bloodType;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}
