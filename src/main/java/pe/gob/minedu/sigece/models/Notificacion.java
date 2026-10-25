package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pe.gob.minedu.sigece.enums.CanalNotificacion;
import pe.gob.minedu.sigece.enums.EstadoEnvio;
import pe.gob.minedu.sigece.enums.OrigenNotificacion;
import pe.gob.minedu.sigece.enums.TipoNotificacion;

import java.time.OffsetDateTime;

@Entity
@Table(name = "notificaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incidente_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_notif_incidente"))
    private Incidente incidente;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "estudiante_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_notif_estudiante")
    )
    private Estudiante estudiante;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "apoderado_id",
            foreignKey = @ForeignKey(name = "fk_notif_apoderado"))
    private Apoderado apoderado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "accion_id",
            foreignKey = @ForeignKey(name = "fk_notif_accion")
    )
    private AccionSeguimiento accion;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "plantilla_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_notif_plantilla")
    )
    private PlantillaMensaje plantilla;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enviado_por_id",
            foreignKey = @ForeignKey(name = "fk_notif_enviado_por"))
    private Usuario enviadoPor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_notificacion", nullable = false, length = 30)
    private TipoNotificacion tipoNotificacion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "origen", nullable = false, length = 15)
    private OrigenNotificacion origen = OrigenNotificacion.AUTOMATICA;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "canal", nullable = false, length = 20)
    private CanalNotificacion canal = CanalNotificacion.WHATSAPP;

    @Size(max = 150)
    @Column(name = "destino", length = 150)
    private String destino;

    @NotBlank
    @Size(max = 150)
    @Column(name = "asunto", length = 150)
    private String asunto;

    @NotBlank
    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fecha_envio")
    private OffsetDateTime fechaEnvio;

    @Column(name = "fecha_entrega")
    private OffsetDateTime fechaEntrega;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_envio", nullable = false, length = 20)
    private EstadoEnvio estadoEnvio = EstadoEnvio.PENDIENTE;

    @NotNull
    @Min(0)
    @Column(name = "intentos_envio", nullable = false)
    private Integer intentosEnvio = 0;

    @Size(max = 500)
    @Column(name = "detalle_resultado", length = 500)
    private String detalleResultado;

    @Size(max = 150)
    @Column(name = "id_transaccion_externo", length = 150)
    private String idTransaccionExterno;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}