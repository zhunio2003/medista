package ec.insti.medista.patient.internal.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import ec.insti.medista.patient.internal.entity.Patient;
import org.springframework.data.domain.Pageable;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    // RF-M1-02 - validar cédula duplicado
    boolean existsByCedula(String cedula);

    // RF-M1-04 - búsqueda exacta por cédula
    Optional<Patient> findByCedula(String cedula);

    // RF-M1-04 - búsqueda por nombre y apellido (tolerante, ignora mayúsculas)
    Page<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);

    // RF-M1-01 - validar userId no este vinculado
    boolean existsByUserId(Long userId);

}