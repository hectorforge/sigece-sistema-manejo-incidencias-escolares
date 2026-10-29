package pe.gob.minedu.sigece.mappers;

import pe.gob.minedu.sigece.dtos.seguimiento.AccionSeguimientoRequestDto;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionSeguimientoResponseDto;
import pe.gob.minedu.sigece.enums.EstadoAccion;
import pe.gob.minedu.sigece.models.AccionSeguimiento;
import pe.gob.minedu.sigece.models.Incidente;
import pe.gob.minedu.sigece.models.Usuario;

import java.time.OffsetDateTime;

public final class AccionSeguimientoMapper {
    private AccionSeguimientoMapper() {}

    public static AccionSeguimiento toEntity(AccionSeguimientoRequestDto dto, Incidente incidente, Usuario usuario) {
        AccionSeguimiento a = new AccionSeguimiento();
        a.setIncidente(incidente);
        a.setUsuario(usuario);
        a.setTipoAccion(dto.tipoAccion());
        a.setDescripcion(dto.descripcion().trim());
        a.setFechaAccion(dto.fechaAccion() != null ? dto.fechaAccion() : OffsetDateTime.now());
        a.setFechaProgramada(dto.fechaProgramada());
        a.setFechaProximaRevision(dto.fechaProximaRevision());
        a.setEstadoAccion(EstadoAccion.PENDIENTE);
        return a;
    }

    public static AccionSeguimientoResponseDto toDto(AccionSeguimiento a) {
        return new AccionSeguimientoResponseDto(
                a.getId(),
                a.getIncidente().getId(),
                a.getUsuario().getId(),
                a.getUsuario().getNombres() + " " + a.getUsuario().getApellidos(),
                a.getTipoAccion(),
                a.getDescripcion(),
                a.getFechaAccion(),
                a.getFechaProximaRevision(),
                a.getEstadoAccion(),
                a.getFechaCompletada(),
                a.getCreatedAt()
        );
    }
}