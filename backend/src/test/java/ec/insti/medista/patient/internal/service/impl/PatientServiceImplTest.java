package ec.insti.medista.patient.internal.service.impl;

import ec.insti.medista.administration.api.service.CareerCatalogService;
import ec.insti.medista.common.exception.ConflictException;
import ec.insti.medista.patient.api.dto.request.CreatePatientRequest;
import ec.insti.medista.patient.internal.mapper.PatientMapper;
import ec.insti.medista.patient.internal.repository.PatientBackgroundRepository;
import ec.insti.medista.patient.internal.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

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

    @Test
    void create_whenCedulaAlreadyExists_throwsConflictAndSavesNothing() {

        CreatePatientRequest request = validCreateRequest();

        when(patientRepository.existsByCedula("0102030405")).thenReturn(true);

        assertThatThrownBy(() -> patientService.create(request, 1L))
            .isInstanceOf(ConflictException.class)
            .hasMessageContaining("0102030405");

        verify(patientRepository, never()).save(any());
    }


}
