package pe.gob.minedu.sigece.dtos.alertas;

import pe.gob.minedu.sigece.enums.EstadoAlerta;
import pe.gob.minedu.sigece.enums.TipoAlerta;

import java.time.LocalDate;

public record AlertaFiltroRequestDto(
        TipoAlerta tipoAlerta,
        EstadoAlerta estadoAlerta,
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        Integer page,
        Integer size
) {
}