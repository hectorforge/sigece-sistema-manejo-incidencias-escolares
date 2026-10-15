package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "incidente_adjuntos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncidenteAdjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "incidente_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_adjuntos_incidente"))
    private Incidente incidente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accion_id",
            foreignKey = @ForeignKey(name = "fk_adjuntos_accion"))
    private AccionSeguimiento accion;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subido_por_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_adjuntos_usuario"))
    private Usuario subidoPor;

    @NotBlank
    @Size(max = 255)
    @Column(name = "nombre_original", nullable = false, length = 255)
    private String nombreOriginal;

    @NotBlank
    @Size(max = 255)
    @Column(name = "nombre_almacenamiento", nullable = false, unique = true, length = 255)
    private String nombreAlmacenamiento;

    @NotBlank
    @Size(min = 3, max = 500)
    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @NotBlank
    @Size(max = 100)
    @Column(name = "tipo_mime", nullable = false, length = 100)
    private String tipoMime;

    @NotNull
    @Min(1)
    @Max(10485760) // 10 MB
    @Column(name = "tamano_bytes", nullable = false)
    private Long tamanoBytes;

    @NotBlank
    @Column(name = "url_storage", nullable = false, columnDefinition = "TEXT")
    private String urlStorage;

    @NotBlank
    @Pattern(regexp = "^[0-9a-fA-F]{64}$")
    @Size(max = 64)
    @Column(name = "hash_sha256", nullable = false, length = 64)
    private String hashSha256;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_eliminacion")
    private OffsetDateTime fechaEliminacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eliminado_por_id",
            foreignKey = @ForeignKey(name = "fk_adjuntos_eliminado_por"))
    private Usuario eliminadoPor;

    @Size(max = 255)
    @Column(name = "motivo_eliminacion", length = 255)
    private String motivoEliminacion;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}