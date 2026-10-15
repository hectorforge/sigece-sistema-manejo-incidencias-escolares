package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import pe.gob.minedu.sigece.enums.AccionAuditoria;
import pe.gob.minedu.sigece.enums.EntidadAuditoria;
import pe.gob.minedu.sigece.enums.ResultadoAuditoria;

import java.time.OffsetDateTime;

@Entity
@Table(name = "auditoria_eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "fecha_evento", nullable = false, updatable = false)
    private OffsetDateTime fechaEvento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id",
            foreignKey = @ForeignKey(name = "fk_auditoria_usuario"))
    private Usuario usuario;

    @Size(max = 50)
    @Column(name = "username_intentado", length = 50)
    private String usernameIntentado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "accion", nullable = false, length = 30)
    private AccionAuditoria accion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "entidad", nullable = false, length = 30)
    private EntidadAuditoria entidad;

    @Size(max = 64)
    @Column(name = "entidad_id", length = 64)
    private String entidadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incidente_id",
            foreignKey = @ForeignKey(name = "fk_auditoria_incidente"))
    private Incidente incidente;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "valor_anterior", columnDefinition = "jsonb")
    private String valorAnterior;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "valor_nuevo", columnDefinition = "jsonb")
    private String valorNuevo;

    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Size(max = 45)
    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @Size(max = 255)
    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "resultado", nullable = false, length = 15)
    private ResultadoAuditoria resultado = ResultadoAuditoria.EXITO;
}