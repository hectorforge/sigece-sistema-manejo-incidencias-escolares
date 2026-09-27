package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pe.gob.minedu.sigece.domain.enums.RolInvolucrado;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Relación N:M entre {@link Incidente} y {@link Estudiante}, indicando
 * el rol del estudiante en el caso (víctima, agresor o testigo).
 * Esta tabla es la base para el análisis de patrones de reincidencia
 * exigido por el personal directivo y de tutoría.
 */
@Entity
@Table(
    name = "incidente_involucrados",
    uniqueConstraints = @UniqueConstraint(columnNames = {"incidente_id", "estudiante_id", "rol_involucrado"})
)
public class IncidenteInvolucrado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incidente_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_involucrados_incidente"))
    @JdbcTypeCode(SqlTypes.UUID)
    private Incidente incidente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudiante_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_involucrados_estudiante"))
    private Estudiante estudiante;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_involucrado", nullable = false, length = 20)
    private RolInvolucrado rolInvolucrado;

    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected IncidenteInvolucrado() {
        // Requerido por JPA
    }

    public IncidenteInvolucrado(Incidente incidente, Estudiante estudiante, RolInvolucrado rolInvolucrado) {
        this.incidente = incidente;
        this.estudiante = estudiante;
        this.rolInvolucrado = rolInvolucrado;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Incidente getIncidente() {
        return incidente;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public RolInvolucrado getRolInvolucrado() {
        return rolInvolucrado;
    }

    public void setRolInvolucrado(RolInvolucrado rolInvolucrado) {
        this.rolInvolucrado = rolInvolucrado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IncidenteInvolucrado)) return false;
        IncidenteInvolucrado that = (IncidenteInvolucrado) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
