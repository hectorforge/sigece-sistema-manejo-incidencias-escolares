package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pe.gob.minedu.sigece.domain.enums.EstadoIncidente;
import pe.gob.minedu.sigece.domain.enums.GravedadIncidente;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Caso de convivencia escolar (entidad agregado raíz del dominio).
 * Se identifica con UUID en lugar de secuencia numérica para evitar
 * la enumeración/exposición de casos sensibles (buenas prácticas de
 * seguridad de la información aplicadas al tratamiento de datos de
 * menores de edad, Ley N.° 29733).
 */
@Entity
@Table(name = "incidentes")
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "codigo_caso", nullable = false, unique = true, length = 30)
    private String codigoCaso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_incidencia_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_incidentes_tipo"))
    private TipoIncidencia tipoIncidencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "gravedad", nullable = false, length = 20)
    private GravedadIncidente gravedad;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "lugar_ocurrencia", length = 150)
    private String lugarOcurrencia;

    @Column(name = "fecha_ocurrencia", nullable = false)
    private LocalDateTime fechaOcurrencia;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoIncidente estado = EstadoIncidente.REGISTRADO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reportado_por_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_incidentes_reportado_por"))
    private Usuario reportadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_actual_id",
            foreignKey = @ForeignKey(name = "fk_incidentes_responsable"))
    private Usuario responsableActual;

    @Column(name = "reincidencia", nullable = false)
    private boolean reincidencia = false;

    @Column(name = "reportado_siseve", nullable = false)
    private boolean reportadoSiseve = false;

    @Column(name = "fecha_reporte_siseve")
    private LocalDateTime fechaReporteSiseve;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "incidente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncidenteInvolucrado> involucrados = new ArrayList<>();

    @OneToMany(mappedBy = "incidente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccionSeguimiento> accionesSeguimiento = new ArrayList<>();

    @OneToMany(mappedBy = "incidente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notificacion> notificaciones = new ArrayList<>();

    protected Incidente() {
        // Requerido por JPA
    }

    public Incidente(String codigoCaso, TipoIncidencia tipoIncidencia, GravedadIncidente gravedad,
                      String descripcion, LocalDateTime fechaOcurrencia, Usuario reportadoPor) {
        this.codigoCaso = codigoCaso;
        this.tipoIncidencia = tipoIncidencia;
        this.gravedad = gravedad;
        this.descripcion = descripcion;
        this.fechaOcurrencia = fechaOcurrencia;
        this.reportadoPor = reportadoPor;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.fechaRegistro = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /** Marca el caso como derivado formalmente al Sistema SíSeVe del MINEDU. */
    public void derivarASiseve() {
        this.reportadoSiseve = true;
        this.fechaReporteSiseve = LocalDateTime.now();
        this.estado = EstadoIncidente.DERIVADO_SISEVE;
    }

    public UUID getId() {
        return id;
    }

    public String getCodigoCaso() {
        return codigoCaso;
    }

    public void setCodigoCaso(String codigoCaso) {
        this.codigoCaso = codigoCaso;
    }

    public TipoIncidencia getTipoIncidencia() {
        return tipoIncidencia;
    }

    public void setTipoIncidencia(TipoIncidencia tipoIncidencia) {
        this.tipoIncidencia = tipoIncidencia;
    }

    public GravedadIncidente getGravedad() {
        return gravedad;
    }

    public void setGravedad(GravedadIncidente gravedad) {
        this.gravedad = gravedad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLugarOcurrencia() {
        return lugarOcurrencia;
    }

    public void setLugarOcurrencia(String lugarOcurrencia) {
        this.lugarOcurrencia = lugarOcurrencia;
    }

    public LocalDateTime getFechaOcurrencia() {
        return fechaOcurrencia;
    }

    public void setFechaOcurrencia(LocalDateTime fechaOcurrencia) {
        this.fechaOcurrencia = fechaOcurrencia;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public EstadoIncidente getEstado() {
        return estado;
    }

    public void setEstado(EstadoIncidente estado) {
        this.estado = estado;
    }

    public Usuario getReportadoPor() {
        return reportadoPor;
    }

    public void setReportadoPor(Usuario reportadoPor) {
        this.reportadoPor = reportadoPor;
    }

    public Usuario getResponsableActual() {
        return responsableActual;
    }

    public void setResponsableActual(Usuario responsableActual) {
        this.responsableActual = responsableActual;
    }

    public boolean isReincidencia() {
        return reincidencia;
    }

    public void setReincidencia(boolean reincidencia) {
        this.reincidencia = reincidencia;
    }

    public boolean isReportadoSiseve() {
        return reportadoSiseve;
    }

    public LocalDateTime getFechaReporteSiseve() {
        return fechaReporteSiseve;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<IncidenteInvolucrado> getInvolucrados() {
        return involucrados;
    }

    public List<AccionSeguimiento> getAccionesSeguimiento() {
        return accionesSeguimiento;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Incidente)) return false;
        Incidente that = (Incidente) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Incidente{id=" + id + ", codigoCaso='" + codigoCaso + "', estado=" + estado + "}";
    }
}
