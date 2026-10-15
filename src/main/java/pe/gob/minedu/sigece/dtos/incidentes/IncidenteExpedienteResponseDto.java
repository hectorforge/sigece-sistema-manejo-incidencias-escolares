package pe.gob.minedu.sigece.dtos.incidentes;

import pe.gob.minedu.sigece.dtos.adjuntos.AdjuntoResponseDto;
import pe.gob.minedu.sigece.dtos.involucrados.InvolucradoResponseDto;
import pe.gob.minedu.sigece.dtos.notificaciones.NotificacionResponseDto;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionSeguimientoResponseDto;

import java.util.List;

public record IncidenteExpedienteResponseDto(
        IncidenteResponseDto incidente,
        List<InvolucradoResponseDto> involucrados,
        List<AccionSeguimientoResponseDto> seguimiento,
        List<AdjuntoResponseDto> evidencias,
        List<NotificacionResponseDto> notificaciones
) {
}