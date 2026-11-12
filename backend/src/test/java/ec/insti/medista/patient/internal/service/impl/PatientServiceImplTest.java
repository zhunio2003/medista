package ec.insti.medista.patient.internal.service.impl;

import ec.insti.medista.administration.api.service.CareerCatalogService;
import ec.insti.medista.common.exception.BusinessRuleViolationException;
import ec.insti.medista.common.exception.ConflictException;
import ec.insti.medista.common.exception.ResourceNotFoundException;
import ec.insti.medista.patient.api.dto.request.CreatePatientRequest;
import ec.insti.medista.patient.api.dto.request.UpdatePatientRequest;
import ec.insti.medista.patient.api.dto.response.PatientResponse;
import ec.insti.medista.patient.internal.entity.Patient;
import ec.insti.medista.patient.internal.entity.PatientBackground;
import ec.insti.medista.patient.internal.mapper.PatientMapper;
import ec.insti.medista.patient.internal.repository.PatientBackgroundRepository;
import ec.insti.medista.patient.internal.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private PatientBackgroundRepository patientBackgroundRepository;
    @Mock
    private CareerCatalogService careerCatalogService;
    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private CreatePatientRequest validCreateRequest() {
        return new CreatePatientRequest(
                null, 3L, "0102030405", "Carlos", "Vásquez", "Quinto",
                "Av. Solano", "El Vergel", "Sucre", "Cuenca", "Azuay",
                "0987654321", LocalDate.of(2003, 8, 14), "Cuenca", "Ecuador",
                "MALE", "SINGLE", "O_POSITIVE"
        );
    }

    private CreatePatientRequest validCreateRequestWithUser(Long userId) {
        return new CreatePatientRequest(
                userId, 3L, "0102030405", "Carlos", "Vásquez", "Quinto",
                "Av. Solano", "El Vergel", "Sucre", "Cuenca", "Azuay",
                "0987654321", LocalDate.of(2003, 8, 14), "Cuenca", "Ecuador",
                "MALE", "SINGLE", "O_POSITIVE"
        );
    }

    private UpdatePatientRequest validUpdateRequest(Long careerId) {
        return new UpdatePatientRequest(
                careerId, "Carlos", "Vásquez", "Sexto",
                "Av. Solano", "El Vergel", "Sucre", "Cuenca", "Azuay",
                "0987654321", LocalDate.of(2003, 8, 14), "Cuenca", "Ecuador",
                "MALE", "SINGLE", "O_POSITIVE"
        );
    }

    @Test
    void create_whenCedulaAlreadyExists_throwsConflictAndSavesNothing() {

        CreatePatientRequest request = validCreateRequest();

        when(patientRepository.existsByCedula("0102030405")).thenReturn(true);

        assertThatThrownBy(() -> patientService.create(request, 1L))
            .isInstanceOf(ConflictException.class)
            .hasMessageContaining("0102030405");

        verify(patientRepository, never()).save(any());
    }

    @Test
    void create_whenCareerIsInactive_throwsBusinessRuleAndSavesNothing() {
        CreatePatientRequest request = validCreateRequest();

        when(patientRepository.existsByCedula("0102030405")).thenReturn(false);
        when(careerCatalogService.existsActiveById(request.careerId())).thenReturn(false);

        assertThatThrownBy(() -> patientService.create(request, 1L))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("inactiva");

        verify(patientRepository, never()).save(any());
        verifyNoInteractions(patientBackgroundRepository);

    }

    @Test
    void create_whenUserIdAlreadyLinked_throwsConflictAndSavesNothing() {

        CreatePatientRequest request = validCreateRequestWithUser(123L);

        when(patientRepository.existsByCedula(request.cedula())).thenReturn(false);
        when(careerCatalogService.existsActiveById(request.careerId())).thenReturn(true);
        when(patientRepository.existsByUserId(request.userId())).thenReturn(true);

        assertThatThrownBy(() -> patientService.create(request, 1L))
            .isInstanceOf(ConflictException.class)
            .hasMessageContaining("vinculado");

        verify(patientRepository, never()).save(any());
        verifyNoInteractions(patientBackgroundRepository);

    }

    @Test
    void create_whenDataIsValid_savesPatientAndEmptyBackground() {

        CreatePatientRequest request = validCreateRequest();
        Patient entity = new Patient();
        Patient saved = Patient.builder().id(7L).build();
        PatientResponse expected = PatientResponse.builder().id(7L).build();

        when(patientRepository.existsByCedula(request.cedula())).thenReturn(false);
        when(careerCatalogService.existsActiveById(request.careerId())).thenReturn(true);
        when(patientMapper.toEntity(request)).thenReturn(entity);
        when(patientRepository.save(entity)).thenReturn(saved);
        when(careerCatalogService.findNameById(request.careerId())).thenReturn("Desarrollo de Software");
        when(patientMapper.toPatientResponse(saved, "Desarrollo de Software")).thenReturn(expected);

        PatientResponse result = patientService.create(request, 1L);

        assertThat(result).isSameAs(expected);

        ArgumentCaptor<PatientBackground> captor = ArgumentCaptor.forClass(PatientBackground.class);
        verify(patientBackgroundRepository).save(captor.capture());
        assertThat(captor.getValue().getPatientId()).isEqualTo(7L);
        assertThat(captor.getValue().getUpdatedBy()).isEqualTo(1L);

        verify(patientRepository, never()).existsByUserId(any());

    }

    @Test
    void deactivate_whenPatientIsActive_setsIsActiveToFalse() {

        Patient patient = Patient.builder().id(5L).isActive(true).build();

        when(patientRepository.findById(5L)).thenReturn(Optional.of(patient));

        patientService.deactivate(5L);

        assertThat(patient.getIsActive()).isFalse();

    }

    @Test
    void deactivate_whenPatientDoesNotExist_throwsNotFound() {

        when(patientRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.deactivate(5L))
            .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void deactivate_whenPatientIsAlreadyInactive_throwsConflict() {

        Patient patient = Patient.builder().id(5L).isActive(false).build();

        when(patientRepository.findById(5L)).thenReturn(Optional.of(patient));

        assertThatThrownBy(() -> patientService.deactivate(5L))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("desactivado");

        assertThat(patient.getIsActive()).isFalse();

    }

    @Test
    void findById_whenPatientDoesNotExist_throwsNotFoundWithoutCallingCatalog() {

        when(patientRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.findById(5L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("5");

        verifyNoInteractions(careerCatalogService);

    }

    @Test
    void findById_whenPatientExists_returnsResponseWithCareerName() {

        Patient patient = Patient.builder().id(5L).careerId(3L).build();
        PatientResponse expected = PatientResponse.builder().id(5L).build();

        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(careerCatalogService.findNameById(3L)).thenReturn("Desarrollo de Software");
        when(patientMapper.toPatientResponse(patient, "Desarrollo de Software")).thenReturn(expected);

        PatientResponse result = patientService.findById(5L);

        assertThat(result).isSameAs(expected);

    }


    @Test
    void update_whenCareerUnchanged_doesNotValidateCareerAndSaves() {

        Patient patient = Patient.builder().id(5L).careerId(3L).build();
        UpdatePatientRequest request = validUpdateRequest(patient.getCareerId());
        PatientResponse expected = PatientResponse.builder().id(5L).build();

        when(patientRepository.findById(5L)).thenReturn(Optional.of(patient));
        when(careerCatalogService.findNameById(patient.getCareerId())).thenReturn("Desarrollo de Software");
        when(patientMapper.toPatientResponse(patient, "Desarrollo de Software")).thenReturn(expected);

        PatientResponse result =  patientService.update(5L, request);

        assertThat(result).isSameAs(expected);
        verify(careerCatalogService, never()).existsActiveById(any());
        verify(patientMapper).updatePatient(request, patient);
        verify(patientRepository).saveAndFlush(patient);

    }

    @Test
    void update_whenCareerChanged_validateCareerAndSaves() {

        Patient patient = Patient.builder().id(5L).careerId(3L).build();
        UpdatePatientRequest request = validUpdateRequest(8L);
        PatientResponse expected = PatientResponse.builder().id(5L).build();

        when(patientRepository.findById(5L)).thenReturn(Optional.of(patient));
        when(careerCatalogService.existsActiveById(request.careerId())).thenReturn(true);
        when(careerCatalogService.findNameById(request.careerId())).thenReturn("Física");
        when(patientMapper.toPatientResponse(patient, "Física")).thenReturn(expected);

        PatientResponse result = patientService.update(5L, request);

        assertThat(result).isSameAs(expected);
        verify(careerCatalogService).existsActiveById(request.careerId());
        verify(patientMapper).updatePatient(request, patient);
        verify(patientRepository).saveAndFlush(patient);

    }

}
