package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.gob.minedu.sigece.enums.CategoriaSiseve;
import pe.gob.minedu.sigece.enums.Gravedad;

@Entity
@Table(name = "tipos_incidencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoIncidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_siseve", nullable = false, length = 50)
    private CategoriaSiseve categoriaSiseve;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gravedad_base", nullable = false, length = 20)
    private Gravedad gravedadBase = Gravedad.LEVE;

    @Size(max = 255)
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}