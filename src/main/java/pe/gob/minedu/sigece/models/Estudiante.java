package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pe.gob.minedu.sigece.enums.Nivel;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Sujeto de protección especial. El tratamiento de estos datos debe
 * cumplir la Ley N.° 29733 (Protección de Datos Personales) por tratarse
 * de información de menores de edad.
 */
@Entity
@Table(name = "estudiantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(name = "codigo_siagie", nullable = false, unique = true, length = 20)
    private String codigoSiagie;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @NotBlank
    @Size(max = 100)
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @NotNull
    @Past
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

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

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}