package ec.insti.medista.administration.internal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "careers")
@Audited
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 150)
    private String name;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;
}
