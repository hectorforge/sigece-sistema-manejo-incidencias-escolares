package pe.gob.minedu.sigece.dtos.seguimiento;

import pe.gob.minedu.sigece.enums.EstadoAccion;
import pe.gob.minedu.sigece.enums.TipoAccion;

import java.time.LocalDate;

public record AccionFiltroRequestDto(
        TipoAccion tipoAccion,
        EstadoAccion estadoAccion,
        LocalDate fechaDesde,
        LocalDate fechaHasta
) {
}