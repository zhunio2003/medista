package ec.insti.medista.security.internal.repository;

import java.time.OffsetDateTime;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.insti.medista.security.internal.entity.AuditLog;
import ec.insti.medista.security.internal.entity.enums.AuditAction;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{

    Page<AuditLog> findByUserId(Long userId, Pageable pageable);

    Page<AuditLog> findByAction(AuditAction action, Pageable pageable);

    Page<AuditLog> findByCreatedAtBetween(
        OffsetDateTime from,
        OffsetDateTime to,
        Pageable pageable
    );
 
}
