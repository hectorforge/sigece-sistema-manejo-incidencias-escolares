package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pe.gob.minedu.sigece.enums.*;

import java.time.OffsetDateTime;
import java.util.UUID;

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
    @Pattern(regexp = "^INC-[0-9]{4}-[0-9]{6}$")
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
    @Enumerated(EnumType.STRING)
    @Column(name = "medio_ocurrencia", nullable = false, length = 20)
    private MedioOcurrencia medioOcurrencia = MedioOcurrencia.PRESENCIAL;

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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "responsable_actual_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_incidentes_responsable")
    )
    private Usuario responsableActual;

    @NotNull
    @Column(name = "notificar_apoderado", nullable = false)
    private Boolean notificarApoderado = false;

    @Column(name = "fecha_resolucion")
    private OffsetDateTime fechaResolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "resuelto_por_id",
            foreignKey = @ForeignKey(name = "fk_incidentes_resuelto_por")
    )
    private Usuario resueltoPor;

    @Column(name = "acta_cierre", columnDefinition = "TEXT")
    private String actaCierre;

    @Column(name = "conclusiones_finales", columnDefinition = "TEXT")
    private String conclusionesFinales;

    @Column(name = "fecha_cierre")
    private OffsetDateTime fechaCierre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cerrado_por_id",
            foreignKey = @ForeignKey(name = "fk_incidentes_cerrado_por"))
    private Usuario cerradoPor;

    @NotNull
    @Column(name = "reportado_siseve", nullable = false)
    private Boolean reportadoSiseve = false;

    @Column(name = "fecha_reporte_siseve")
    private OffsetDateTime fechaReporteSiseve;

    @Size(max = 50)
    @Column(name = "codigo_siseve", length = 50)
    private String codigoSiseve;

    @Column(name = "observaciones_siseve", columnDefinition = "TEXT")
    private String observacionesSiseve;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "derivado_por_id",
            foreignKey = @ForeignKey(name = "fk_incidentes_derivado_por"))
    private Usuario derivadoPor;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}