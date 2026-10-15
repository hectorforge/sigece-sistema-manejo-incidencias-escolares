package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.alertas.AlertaResponseDto;
import pe.gob.minedu.sigece.models.Alerta;

public final class AlertaMapper {
    private AlertaMapper() {}

    public static AlertaResponseDto toDto(Alerta a) {
        return new AlertaResponseDto(
                a.getId(),
                a.getIncidente().getId(),
                a.getIncidente().getCodigoCaso(),
                a.getEstudiante() != null ? a.getEstudiante().getId() : null,
                a.getEstudiante() != null ? a.getEstudiante().getNombres() + " " + a.getEstudiante().getApellidos() : null,
                a.getUsuarioDestino() != null ? a.getUsuarioDestino().getId() : null,
                a.getTipoAlerta(),
                a.getOrigen(),
                a.getMensaje(),
                a.getParametros(),
                a.getEstadoAlerta(),
                a.getFechaGeneracion(),
                a.getFechaResolucion(),
                a.getResueltaPor() != null ? a.getResueltaPor().getNombres() + " " + a.getResueltaPor().getApellidos() : null,
                a.getObservacionResolucion()
        );
    }
}