package ec.insti.medista.patient.internal.repository;

import java.util.List;
import java.util.Optional;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import ec.insti.medista.patient.internal.entity.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {

    // RF-M1-02 - validar cédula duplicado
    boolean existsByCedula(String cedula);

    // RF-M1-04 - búsqueda exacta por cédula
    Optional<Patient> findByCedula(String cedula);

    // RF-M1-04 - búsqueda por nombre y apellido (tolerante, ignora mayúsculas)
    Page<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);

    // RF-M1-01 - validar userId no este vinculado
    boolean existsByUserId(Long userId);

    // RF-M1-04 - búsqueda cedula, nombre, apellido y carrera
    @Query(value = """
        SELECT p.id AS "id",
            p.cedula AS "cedula",
            p.first_name AS "firstName",
            p.last_name AS "lastName",
            p.career_id AS "careerId",
            CAST(GREATEST(similarity(p.first_name || ' ' || p.last_name, :query),
                similarity(p.cedula, :query)) AS double precision) AS "similarity"
        FROM patients p 
        WHERE p.is_active = true
            AND ((p.first_name  || ' ' || p.last_name) % :query
                OR p.cedula % :query
                OR p.cedula LIKE CONCAT(:query, '%'))
        ORDER BY "similarity" DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<PatientSearchRow> search(@Param("query") String query, @Param("limit") int limit);

}
