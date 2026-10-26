package ec.insti.medista.patient.api.service;

import ec.insti.medista.common.dto.PageResponse;
import ec.insti.medista.patient.api.dto.request.CreatePatientRequest;
import ec.insti.medista.patient.api.dto.request.UpdatePatientBackgroundRequest;
import ec.insti.medista.patient.api.dto.request.UpdatePatientRequest;
import ec.insti.medista.patient.api.dto.response.PatientBackgroundResponse;
import ec.insti.medista.patient.api.dto.response.PatientResponse;
import ec.insti.medista.patient.api.dto.response.PatientSearchResponse;
import ec.insti.medista.patient.api.dto.response.PatientSummaryResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientService {

    PatientResponse create(CreatePatientRequest request);
    PatientResponse findById(Long id);
    PageResponse<PatientSummaryResponse> findAll(Long careerId, String gender, Boolean isActive, Pageable pageable);
    List<PatientSearchResponse> search(String text, int limit);
    PatientResponse update(Long id, UpdatePatientRequest request);
    void deactivate(Long id);
    PatientBackgroundResponse getBackground(Long patientId);
    PatientBackgroundResponse updateBackground(Long patientId, UpdatePatientBackgroundRequest request, Long updatedBy);

}
