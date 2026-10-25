package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pe.gob.minedu.sigece.enums.Nivel;

import java.time.OffsetDateTime;

@Entity
@Table(name = "tutor_aula",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_tutor_aula",
                        columnNames = {
                                "usuario_id",
                                "nivel",
                                "grado",
                                "seccion",
                                "anio_escolar"
                        }
                )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TutorAula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "usuario_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_tutor_aula_usuario")
    )
    private Usuario usuario;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel", nullable = false, length = 20)
    private Nivel nivel;

    @NotBlank
    @Size(max = 20)
    @Column(name = "grado", nullable = false, length = 20)
    private String grado;

    @NotBlank
    @Size(max = 10)
    @Column(name = "seccion", nullable = false, length = 10)
    private String seccion;

    @Min(2000)
    @Max(2100)
    @Column(name = "anio_escolar", nullable = false)
    private int anioEscolar;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
