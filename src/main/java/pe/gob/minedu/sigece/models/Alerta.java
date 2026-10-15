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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import pe.gob.minedu.sigece.enums.EstadoAlerta;
import pe.gob.minedu.sigece.enums.OrigenAlerta;
import pe.gob.minedu.sigece.enums.TipoAlerta;

import java.time.OffsetDateTime;

@Entity
@Table(name = "alertas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incidente_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_alertas_incidente"))
    private Incidente incidente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id",
            foreignKey = @ForeignKey(name = "fk_alertas_estudiante"))
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_destino_id",
            foreignKey = @ForeignKey(name = "fk_alertas_usuario_destino"))
    private Usuario usuarioDestino;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_alerta", nullable = false, length = 30)
    private TipoAlerta tipoAlerta;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "origen", nullable = false, length = 15)
    private OrigenAlerta origen = OrigenAlerta.AUTOMATICA;

    @NotBlank
    @Size(max = 500)
    @Column(name = "mensaje", nullable = false, length = 500)
    private String mensaje;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "parametros", columnDefinition = "jsonb")
    private String parametros;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_alerta", nullable = false, length = 15)
    private EstadoAlerta estadoAlerta = EstadoAlerta.PENDIENTE;

    @CreationTimestamp
    @Column(name = "fecha_generacion", nullable = false, updatable = false)
    private OffsetDateTime fechaGeneracion;

    @Column(name = "fecha_resolucion")
    private OffsetDateTime fechaResolucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resuelta_por_id",
            foreignKey = @ForeignKey(name = "fk_alertas_resuelta_por"))
    private Usuario resueltaPor;

    @Size(max = 255)
    @Column(name = "observacion_resolucion", length = 255)
    private String observacionResolucion;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}