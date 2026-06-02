package ec.insti.medista.patient.internal.entity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

import ec.insti.medista.patient.internal.entity.enums.BloodType;
import ec.insti.medista.patient.internal.entity.enums.Gender;
import ec.insti.medista.patient.internal.entity.enums.MaritalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "patients")
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "career_id", nullable = false)
    private Long careerId;

    @Column(nullable = false, unique = true, length = 10)
    private String cedula;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, length = 20)
    private String semester;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(length = 100)
    private String neighborhood;

    @Column(length = 100)
    private String parish;

    @Column(nullable = false, length = 100)
    private String canton;

    @Column(nullable = false, length = 100)
    private String province;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(length = 100)
    private String birthPlace;

    @Builder.Default
    @Column(length = 100)
    private String birthCountry = "Ecuador";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition =  "gender_enum")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "marital_status_enum")
    private MaritalStatus maritalStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition  = "blood_type_enum")
    private BloodType bloodType;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

}
