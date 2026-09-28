package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.enums.Gravedad;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Expediente digital central del caso de convivencia escolar.
 * PK de tipo UUID para evitar exponer numeración secuencial sensible
 * en URLs o reportes ante terceros (UGEL, SíSeVe).
 */
@Entity
@Table(name = "incidentes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Size(max = 30)
    @Column(name = "codigo_caso", nullable = false, unique = true, length = 30)
    private String codigoCaso;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_incidencia_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_incidentes_tipo"))
    private TipoIncidencia tipoIncidencia;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gravedad", nullable = false, length = 20)
    private Gravedad gravedad;

    @NotBlank
    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @NotBlank
    @Size(max = 150)
    @Column(name = "lugar_ocurrencia", nullable = false, length = 150)
    private String lugarOcurrencia;

    @NotNull
    @Column(name = "fecha_ocurrencia", nullable = false)
    private OffsetDateTime fechaOcurrencia;

    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private OffsetDateTime fechaRegistro;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 25)
    private EstadoIncidente estado = EstadoIncidente.REGISTRADO;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reportado_por_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_incidentes_reportado_por"))
    private Usuario reportadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsable_actual_id",
            foreignKey = @ForeignKey(name = "fk_incidentes_responsable"))
    private Usuario responsableActual;

    @NotNull
    @Column(name = "reincidencia", nullable = false)
    private Boolean reincidencia = false;

    @NotNull
    @Column(name = "reportado_siseve", nullable = false)
    private Boolean reportadoSiseve = false;

    @Column(name = "fecha_reporte_siseve")
    private OffsetDateTime fechaReporteSiseve;

    @Size(max = 50)
    @Column(name = "codigo_siseve", length = 50)
    private String codigoSiseve;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}