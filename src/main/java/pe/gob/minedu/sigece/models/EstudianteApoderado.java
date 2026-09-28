package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pe.gob.minedu.sigece.enums.Parentesco;

import java.time.OffsetDateTime;

@Entity
@Table(name = "estudiante_apoderado",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_estudiante_apoderado",
                columnNames = {"estudiante_id", "apoderado_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteApoderado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudiante_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_est_apod_estudiante"))
    private Estudiante estudiante;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "apoderado_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_est_apod_apoderado"))
    private Apoderado apoderado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "parentesco", nullable = false, length = 30)
    private Parentesco parentesco;

    @NotNull
    @Column(name = "es_contacto_principal", nullable = false)
    private Boolean esContactoPrincipal = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}