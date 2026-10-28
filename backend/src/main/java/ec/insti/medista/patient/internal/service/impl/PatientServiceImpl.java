package ec.insti.medista.patient.internal.service.impl;

import ec.insti.medista.administration.api.service.CareerCatalogService;
import ec.insti.medista.common.dto.PageResponse;
import ec.insti.medista.common.exception.BusinessRuleViolationException;
import ec.insti.medista.common.exception.ConflictException;
import ec.insti.medista.common.exception.ResourceNotFoundException;
import ec.insti.medista.patient.api.dto.request.CreatePatientRequest;
import ec.insti.medista.patient.api.dto.request.UpdatePatientBackgroundRequest;
import ec.insti.medista.patient.api.dto.request.UpdatePatientRequest;
import ec.insti.medista.patient.api.dto.response.PatientBackgroundResponse;
import ec.insti.medista.patient.api.dto.response.PatientResponse;
import ec.insti.medista.patient.api.dto.response.PatientSearchResponse;
import ec.insti.medista.patient.api.dto.response.PatientSummaryResponse;
import ec.insti.medista.patient.api.service.PatientService;
import ec.insti.medista.patient.internal.entity.Patient;
import ec.insti.medista.patient.internal.entity.PatientBackground;
import ec.insti.medista.patient.internal.mapper.PatientMapper;
import ec.insti.medista.patient.internal.repository.PatientBackgroundRepository;
import ec.insti.medista.patient.internal.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientBackgroundRepository patientBackgroundRepository;
    private final PatientMapper patientMapper;
    private final CareerCatalogService careerCatalogService;

    @Override
    @Transactional
    public PatientResponse create(CreatePatientRequest request, Long createdBy) {

        if (patientRepository.existsByCedula(request.cedula())) {
            throw new ConflictException("Ya existe un paciente con la cédula " +  request.cedula());
        }

        if (!careerCatalogService.existsActiveById(request.careerId())) {
            throw new BusinessRuleViolationException("La carrera no existe o está inactiva");
        }

        if (request.userId() != null && patientRepository.existsByUserId(request.userId())) {
            throw new ConflictException("El usuario ya está vinculado a otro paciente");
        }

        Patient saved = patientMapper.toEntity(request);
        patientRepository.save(saved);

        PatientBackground background = PatientBackground.builder()
                .patientId(saved.getId())
                .updatedBy(createdBy)
                .build();
        patientBackgroundRepository.save(background);

        String careerName = careerCatalogService.findNameById(request.careerId());

        return patientMapper.toPatientResponse(saved, careerName);
    }

    @Override
    public PatientResponse findById(Long id) {

        Patient patient = getPatientOrThrow(id);

        String careerName = careerCatalogService.findNameById(patient.getCareerId());

        return patientMapper.toPatientResponse(patient, careerName);
    }

    @Override
    public PageResponse<PatientSummaryResponse> findAll(Long careerId, String gender, Boolean isActive, Pageable pageable) {
        return null;
    }

    @Override
    public List<PatientSearchResponse> search(String query, int limit) {
        return List.of();
    }

    @Override
    @Transactional
    public PatientResponse update(Long id, UpdatePatientRequest request) {

        Patient patient = getPatientOrThrow(id);

        boolean careerChanged = !patient.getCareerId().equals(request.careerId());
        if (careerChanged && !careerCatalogService.existsActiveById(request.careerId())) {
            throw new BusinessRuleViolationException("La carrera no existe o está inactiva");
        }

        patientMapper.updatePatient(request, patient);
        patientRepository.saveAndFlush(patient);

        String careerName = careerCatalogService.findNameById(request.careerId());

        return patientMapper.toPatientResponse(patient, careerName);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {

        Patient patient = getPatientOrThrow(id);

        if (!patient.getIsActive()) {
            throw new ConflictException("El paciente ya está desactivado");
        }

        patient.setIsActive(false);
    }

    @Override
    public PatientBackgroundResponse getBackground(Long patientId) {
        return null;
    }

    @Override
    public PatientBackgroundResponse updateBackground(Long patientId, UpdatePatientBackgroundRequest request, Long updatedBy) {
        return null;
    }

    private Patient getPatientOrThrow(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado" + id));
    }
}
