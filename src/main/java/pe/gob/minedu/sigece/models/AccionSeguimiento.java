package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pe.gob.minedu.sigece.enums.EstadoAccion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Acción de seguimiento o medida correctiva/formativa aplicada a un
 * {@link Incidente}. Constituye la bitácora de trazabilidad exigida
 * por el D.S. N.° 004-2018-MINEDU para garantizar la intervención
 * oportuna del Comité de Gestión del Bienestar.
 */
@Entity
@Table(name = "acciones_seguimiento")
public class AccionSeguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incidente_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_acciones_incidente"))
    @JdbcTypeCode(SqlTypes.UUID)
    private Incidente incidente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_acciones_usuario"))
    private Usuario usuario;

    @Column(name = "tipo_accion", nullable = false, length = 50)
    private String tipoAccion;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_accion", nullable = false)
    private LocalDateTime fechaAccion;

    @Column(name = "fecha_proxima_revision")
    private LocalDate fechaProximaRevision;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_accion", nullable = false, length = 20)
    private EstadoAccion estadoAccion = EstadoAccion.PENDIENTE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected AccionSeguimiento() {

    }

    public AccionSeguimiento(Incidente incidente, Usuario usuario, String tipoAccion, String descripcion) {
        this.incidente = incidente;
        this.usuario = usuario;
        this.tipoAccion = tipoAccion;
        this.descripcion = descripcion;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.fechaAccion == null) {
            this.fechaAccion = now;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Incidente getIncidente() {
        return incidente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaAccion() {
        return fechaAccion;
    }

    public void setFechaAccion(LocalDateTime fechaAccion) {
        this.fechaAccion = fechaAccion;
    }

    public LocalDate getFechaProximaRevision() {
        return fechaProximaRevision;
    }

    public void setFechaProximaRevision(LocalDate fechaProximaRevision) {
        this.fechaProximaRevision = fechaProximaRevision;
    }

    public EstadoAccion getEstadoAccion() {
        return estadoAccion;
    }

    public void setEstadoAccion(EstadoAccion estadoAccion) {
        this.estadoAccion = estadoAccion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccionSeguimiento)) return false;
        AccionSeguimiento that = (AccionSeguimiento) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
