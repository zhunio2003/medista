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
import ec.insti.medista.patient.internal.entity.enums.Gender;
import ec.insti.medista.patient.internal.mapper.PatientMapper;
import ec.insti.medista.patient.internal.repository.PatientBackgroundRepository;
import ec.insti.medista.patient.internal.repository.PatientRepository;
import ec.insti.medista.patient.internal.repository.PatientSearchRow;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ec.insti.medista.patient.internal.repository.PatientSpecifications.*;

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

        Patient saved = patientRepository.save(patientMapper.toEntity(request));

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

        Gender genderEnum = gender == null ? null : Gender.valueOf(gender);

        Specification<Patient> specs = Specification.allOf(hasCareer(careerId), hasGender(genderEnum), hasActiveStatus(isActive));

        Page<Patient> page = patientRepository.findAll(specs, pageable);

        Set<Long> careerIds = page.getContent().stream()
                .map(Patient::getCareerId)
                .collect(Collectors.toSet());

        Map<Long, String> careerNames = careerCatalogService.findNamesByIds(careerIds);

        Page<PatientSummaryResponse> result = page.map(
                patient -> patientMapper.toSummaryResponse(patient, careerNames.get(patient.getCareerId()))
        );

        return PageResponse.from(result);
    }

    @Override
    public List<PatientSearchResponse> search(String query, int limit) {

        List<PatientSearchRow> rows = patientRepository.searchBySimilarity(query.trim(), Math.min(limit, 25));

        Set<Long> careerIds = rows.stream()
                .map(PatientSearchRow::getCareerId)
                .collect(Collectors.toSet());

        Map<Long, String> careerNames = careerCatalogService.findNamesByIds(careerIds);

        return rows.stream()
                .map(row -> patientMapper.toSearchResponse(row, careerNames.get(row.getCareerId())))
                .toList();

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

        if (!patientBackgroundRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Paciente no encontrado");
        }

        return patientBackgroundRepository.findByPatientId(patientId)
                .map(patientMapper::toBackgroundResponse)
                .orElseGet(() -> PatientBackgroundResponse.builder().build());

    }

    @Override
    @Transactional
    public PatientBackgroundResponse updateBackground(Long patientId, UpdatePatientBackgroundRequest request, Long updatedBy) {

        Patient patient = getPatientOrThrow(patientId);

        boolean hasGynecological = request.gynecological() != null && !request.gynecological().isBlank();
        if (hasGynecological && patient.getGender() != Gender.FEMALE) {
            throw new BusinessRuleViolationException("Los antecedentes ginecológicos solo aplican a pacientes de género femenino");
        }

        PatientBackground background = patientBackgroundRepository.findByPatientId(patientId)
                .orElseGet(() -> PatientBackground.builder().patientId(patientId).build());

        patientMapper.updateBackground(request, background);
        background.setUpdatedBy(updatedBy);

        PatientBackground saved = patientBackgroundRepository.saveAndFlush(background);

        return patientMapper.toBackgroundResponse(saved);
    }

    private Patient getPatientOrThrow(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado" + id));
    }
}
