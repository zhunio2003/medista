package ec.insti.medista.patient.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.insti.medista.patient.internal.entity.PatientBackground;

import java.util.Optional;

public interface PatientBackgroundRepository extends JpaRepository<PatientBackground, Long> {

    // RF-M1-05 - Extraer el acceso directo al historial clínico
    Optional<PatientBackground> findByPatientId(Long patientId);

}