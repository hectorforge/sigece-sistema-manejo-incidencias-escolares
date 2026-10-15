package pe.gob.minedu.sigece.dtos.incidentes;

import java.util.List;

public record CierreChecklistResponseDto(
        boolean todasLasAccionesCompletadas,
        boolean apoderadosNotificados,
        boolean actaCierrePresente,
        boolean cumplePlazosLegales,
        boolean listoParaCierre,
        List<String> observacionesPendientes
) {
}