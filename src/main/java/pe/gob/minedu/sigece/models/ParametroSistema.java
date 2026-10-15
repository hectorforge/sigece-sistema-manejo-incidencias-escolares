package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "parametros_sistema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParametroSistema {

    @Id
    @NotBlank
    @Size(max = 60)
    @Pattern(regexp = "^[A-Z0-9_]+$")
    @Column(name = "clave", nullable = false, length = 60)
    private String clave;

    @NotBlank
    @Size(max = 255)
    @Column(name = "valor", nullable = false, length = 255)
    private String valor;

    @Size(max = 255)
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}