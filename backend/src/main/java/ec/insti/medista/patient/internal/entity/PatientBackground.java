package ec.insti.medista.patient.internal.entity;

import java.time.OffsetDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "patient_background")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientBackground {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false, unique = true)
    private Long patientId;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(columnDefinition = "TEXT")
    private String clinical;

    @Column(columnDefinition = "TEXT")
    private String gynecological;

    @Column(columnDefinition = "TEXT")
    private String traumatological;

    @Column(columnDefinition = "TEXT")
    private String surgical;

    @Column(columnDefinition = "TEXT")
    private String pharmacological;

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

    @UpdateTimestamp
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

}
