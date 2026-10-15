package pe.gob.minedu.sigece.dtos.reportes;

import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.enums.Nivel;

import java.time.LocalDate;

public record ReporteFiltroRequestDto(
        String tipoReporte,
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        Nivel nivel,
        String grado,
        EstadoIncidente estado
) {
}