package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pe.gob.minedu.sigece.domain.enums.CanalNotificacion;
import pe.gob.minedu.sigece.domain.enums.EstadoNotificacion;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Comunicación enviada al apoderado respecto de un {@link Incidente}.
 * Registro auditable orientado a resolver el retraso de comunicación
 * con los padres de familia identificado en el problema de investigación.
 */
@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incidente_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_notif_incidente"))
    @JdbcTypeCode(SqlTypes.UUID)
    private Incidente incidente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "apoderado_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_notif_apoderado"))
    private Apoderado apoderado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enviado_por_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_notif_enviado_por"))
    private Usuario enviadoPor;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal", nullable = false, length = 20)
    private CanalNotificacion canal;

    @Column(name = "asunto", length = 150)
    private String asunto;

    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_envio", nullable = false, length = 20)
    private EstadoNotificacion estadoEnvio = EstadoNotificacion.PENDIENTE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Notificacion() {
        // Requerido por JPA
    }

    public Notificacion(Incidente incidente, Apoderado apoderado, Usuario enviadoPor,
                         CanalNotificacion canal, String mensaje) {
        this.incidente = incidente;
        this.apoderado = apoderado;
        this.enviadoPor = enviadoPor;
        this.canal = canal;
        this.mensaje = mensaje;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /** Marca la notificación como efectivamente enviada al apoderado. */
    public void marcarComoEnviada() {
        this.estadoEnvio = EstadoNotificacion.ENVIADO;
        this.fechaEnvio = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Incidente getIncidente() {
        return incidente;
    }

    public Apoderado getApoderado() {
        return apoderado;
    }

    public Usuario getEnviadoPor() {
        return enviadoPor;
    }

    public CanalNotificacion getCanal() {
        return canal;
    }

    public void setCanal(CanalNotificacion canal) {
        this.canal = canal;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public EstadoNotificacion getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(EstadoNotificacion estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
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
        if (!(o instanceof Notificacion)) return false;
        Notificacion that = (Notificacion) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
