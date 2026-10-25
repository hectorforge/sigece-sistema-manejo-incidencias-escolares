package pe.gob.minedu.sigece.models;

import com.fasterxml.jackson.databind.JsonNode;
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

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "reportes_generados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteGenerado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "tipo_reporte", nullable = false, length = 50)
    private String tipoReporte;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "filtros", columnDefinition = "jsonb")
    private JsonNode filtros;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "indicadores", columnDefinition = "jsonb")
    private JsonNode indicadores;

    @Size(max = 10)
    @Column(name = "formato", length = 10)
    private String formato;

    @Column(name = "archivo_url", columnDefinition = "TEXT")
    private String archivoUrl;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "generado_por_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_reporte_usuario")
    )
    private Usuario generadoPor;

    @CreationTimestamp
    @Column(name = "fecha_generacion", nullable = false, updatable = false)
    private OffsetDateTime fechaGeneracion;
}