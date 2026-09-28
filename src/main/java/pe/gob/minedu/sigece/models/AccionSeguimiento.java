package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pe.gob.minedu.sigece.enums.EstadoAccion;
import pe.gob.minedu.sigece.enums.TipoAccion;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "acciones_seguimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccionSeguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incidente_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_acciones_incidente"))
    private Incidente incidente;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_acciones_usuario"))
    private Usuario usuario;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_accion", nullable = false, length = 50)
    private TipoAccion tipoAccion;

    @NotBlank
    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @CreationTimestamp
    @Column(name = "fecha_accion", nullable = false, updatable = false)
    private OffsetDateTime fechaAccion;

    @Column(name = "fecha_proxima_revision")
    private LocalDate fechaProximaRevision;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_accion", nullable = false, length = 20)
    private EstadoAccion estadoAccion = EstadoAccion.PENDIENTE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}