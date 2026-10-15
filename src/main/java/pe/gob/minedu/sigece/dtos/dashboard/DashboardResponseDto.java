package pe.gob.minedu.sigece.dtos.dashboard;

import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.enums.Gravedad;

import java.util.Map;

public record DashboardResponseDto(
        long totalIncidentes,
        long totalReincidentes,
        long totalDerivadosSiseve,
        long totalCerrados,
        double porcentajeResolucion,
        Map<Gravedad, Long> incidentesPorGravedad,
        Map<EstadoIncidente, Long> incidentesPorEstado,
        Map<String, Long> incidentesPorTipo,
        Map<String, Long> incidentesPorMes
) {
}