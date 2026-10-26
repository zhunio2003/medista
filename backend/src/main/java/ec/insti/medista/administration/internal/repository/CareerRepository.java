package ec.insti.medista.administration.internal.repository;

import ec.insti.medista.administration.internal.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CareerRepository extends JpaRepository<Career, Long> {

    boolean existsByIdAndIsActiveTrue(Long id);
    List<Career> findAllByIdIn(Collection<Long> ids);
}
