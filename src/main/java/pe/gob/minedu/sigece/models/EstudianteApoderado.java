package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad asociativa (tabla intermedia) entre {@link Estudiante} y
 * {@link Apoderado}, con atributos propios de la relación (parentesco,
 * contacto principal para notificaciones).
 */
@Entity
@Table(
    name = "estudiante_apoderado",
    uniqueConstraints = @UniqueConstraint(columnNames = {"estudiante_id", "apoderado_id"})
)
public class EstudianteApoderado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudiante_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_est_apod_estudiante"))
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "apoderado_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_est_apod_apoderado"))
    private Apoderado apoderado;

    @Column(name = "parentesco", nullable = false, length = 30)
    private String parentesco;

    @Column(name = "es_contacto_principal", nullable = false)
    private boolean contactoPrincipal = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected EstudianteApoderado() {
        // Requerido por JPA
    }

    public EstudianteApoderado(Estudiante estudiante, Apoderado apoderado, String parentesco) {
        this.estudiante = estudiante;
        this.apoderado = apoderado;
        this.parentesco = parentesco;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Apoderado getApoderado() {
        return apoderado;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public boolean isContactoPrincipal() {
        return contactoPrincipal;
    }

    public void setContactoPrincipal(boolean contactoPrincipal) {
        this.contactoPrincipal = contactoPrincipal;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstudianteApoderado)) return false;
        EstudianteApoderado that = (EstudianteApoderado) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
