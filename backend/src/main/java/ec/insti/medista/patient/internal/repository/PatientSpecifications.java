package ec.insti.medista.patient.internal.repository;

import ec.insti.medista.patient.internal.entity.Patient;
import ec.insti.medista.patient.internal.entity.enums.Gender;
import org.springframework.data.jpa.domain.Specification;

public final class PatientSpecifications {

    private PatientSpecifications() {}

    public static Specification<Patient> hasCareer(Long careerId) {
        return (root, query, cb) -> careerId == null ? null  : cb.equal(root.get("careerId"), careerId);
    }

    public static Specification<Patient> hasGender(Gender gender) {
        return (root, query, cb) -> gender == null ? null  : cb.equal(root.get("gender"), gender);
    }

    public static Specification<Patient> hasActiveStatus(Boolean isActive) {
        return (root, query, cb) -> isActive == null ? null  : cb.equal(root.get("isActive"), isActive);
    }

}
