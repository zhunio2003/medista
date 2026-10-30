package ec.insti.medista.patient.internal.mapper;

import ec.insti.medista.patient.api.dto.request.CreatePatientRequest;
import ec.insti.medista.patient.api.dto.request.UpdatePatientBackgroundRequest;
import ec.insti.medista.patient.api.dto.request.UpdatePatientRequest;
import ec.insti.medista.patient.api.dto.response.PatientBackgroundResponse;
import ec.insti.medista.patient.api.dto.response.PatientResponse;
import ec.insti.medista.patient.api.dto.response.PatientSummaryResponse;
import ec.insti.medista.patient.internal.entity.Patient;
import ec.insti.medista.patient.internal.entity.PatientBackground;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.Period;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PatientMapper {

    PatientBackgroundResponse toBackgroundResponse(PatientBackground background);

    @Mapping(target = "careerName", source = "careerName")
    @Mapping(target = "age", source = "patient.birthDate", qualifiedByName = "calculateAge")
    PatientResponse toPatientResponse(Patient patient, String careerName);

    @Mapping(target = "careerName", source = "careerName")
    PatientSummaryResponse toSummaryResponse(Patient patient, String careerName);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "birthCountry", defaultValue = "Ecuador")
    Patient toEntity(CreatePatientRequest patient);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cedula", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "birthCountry", nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    void updatePatient(UpdatePatientRequest request, @MappingTarget Patient patient);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void  updateBackground(UpdatePatientBackgroundRequest request, @MappingTarget PatientBackground background);

    @Named("calculateAge")
    default int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

}
