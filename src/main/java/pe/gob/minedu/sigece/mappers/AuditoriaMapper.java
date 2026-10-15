package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.auditoria.AuditoriaResponseDto;
import pe.gob.minedu.sigece.models.AuditoriaEvento;

public final class AuditoriaMapper {
    private AuditoriaMapper() {}

    public static AuditoriaResponseDto toDto(AuditoriaEvento a) {
        return new AuditoriaResponseDto(
                a.getId(),
                a.getFechaEvento(),
                a.getUsuario() != null ? a.getUsuario().getId() : null,
                a.getUsuario() != null ? a.getUsuario().getNombres() + " " + a.getUsuario().getApellidos() : "Sistema/Anónimo",
                a.getUsernameIntentado(),
                a.getAccion(),
                a.getEntidad(),
                a.getEntidadId(),
                a.getIncidente() != null ? a.getIncidente().getId() : null,
                a.getValorAnterior(),
                a.getValorNuevo(),
                a.getDescripcion(),
                a.getIpOrigen(),
                a.getUserAgent(),
                a.getResultado()
        );
    }
}