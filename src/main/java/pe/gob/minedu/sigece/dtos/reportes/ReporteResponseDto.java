package pe.gob.minedu.sigece.dtos.reportes;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record ReporteResponseDto(
        String titulo,
        String institucion,
        OffsetDateTime fechaGeneracion,
        String generadoPor,
        int totalRegistros,
        List<Map<String, Object>> filas
) {
}