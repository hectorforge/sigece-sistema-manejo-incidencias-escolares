package pe.gob.minedu.sigece.dtos.reportes;

import java.util.List;

public record TipoReporteResponseDto(
        String codigo,
        String nombre,
        String descripcion,
        List<String> formatosSoportados
) {
}