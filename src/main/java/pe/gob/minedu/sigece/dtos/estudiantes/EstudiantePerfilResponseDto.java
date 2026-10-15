package pe.gob.minedu.sigece.dtos.estudiantes;

import pe.gob.minedu.sigece.dtos.apoderados.VinculoFamiliarResponseDto;
import pe.gob.minedu.sigece.dtos.incidentes.IncidenteResumenResponseDto;

import java.util.List;

public record EstudiantePerfilResponseDto(
        EstudianteResponseDto estudiante,
        List<VinculoFamiliarResponseDto> apoderados,
        int totalIncidentesHistoricos,
        List<IncidenteResumenResponseDto> ultimosIncidentes
) {
}