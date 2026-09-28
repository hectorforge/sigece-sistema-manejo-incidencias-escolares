package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import pe.gob.minedu.sigece.enums.RolInvolucrado;

import java.time.OffsetDateTime;

/**
 * Mapea a víctimas, agresores y testigos. La regla de exclusión
 * (estudiante XOR usuario) definida en la BD se refuerza aquí
 * con una validación a nivel de objeto (@AssertTrue) y también
 * debe validarse en la capa de servicio antes de persistir.
 */
@Entity
@Table(name = "incidente_involucrados",
        indexes = {
                @Index(name = "idx_involucrados_incidente", columnList = "incidente_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncidenteInvolucrado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incidente_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_involucrados_incidente"))
    private Incidente incidente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id",
            foreignKey = @ForeignKey(name = "fk_involucrados_estudiante"))
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id",
            foreignKey = @ForeignKey(name = "fk_involucrados_usuario"))
    private Usuario usuario;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "rol_involucrado", nullable = false, length = 20)
    private RolInvolucrado rolInvolucrado;

    @Size(max = 255)
    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    /**
     * Espeja la CHECK constraint chk_involucrado_entidad_exclusiva:
     * debe existir un estudiante O un usuario, nunca ambos ni ninguno.
     */
    @AssertTrue(message = "El involucrado debe ser exactamente un estudiante o un usuario (personal), no ambos ni ninguno.")
    public boolean isEntidadExclusivaValida() {
        return (estudiante != null && usuario == null) || (estudiante == null && usuario != null);
    }
}