package pe.gob.minedu.sigece.dtos.reportes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ExportarReporteRequestDto(
        @NotBlank(message = "El tipo de reporte es obligatorio")
        String tipoReporte,

        @NotBlank(message = "El formato de exportación es obligatorio")
        @Pattern(regexp = "^(?i)(PDF|EXCEL)$", message = "El formato debe ser PDF o EXCEL")
        String formato,

        @NotNull(message = "Los filtros son obligatorios")
        ReporteFiltroRequestDto filtro,

        boolean anonimizarDatosMenores
) {
}