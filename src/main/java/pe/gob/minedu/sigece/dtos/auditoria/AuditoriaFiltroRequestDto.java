package pe.gob.minedu.sigece.dtos.auditoria;

import pe.gob.minedu.sigece.enums.AccionAuditoria;
import pe.gob.minedu.sigece.enums.EntidadAuditoria;

import java.time.LocalDate;

public record AuditoriaFiltroRequestDto(
        AccionAuditoria accion,
        EntidadAuditoria entidad,
        Long usuarioId,
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        Integer page,
        Integer size
) {
}