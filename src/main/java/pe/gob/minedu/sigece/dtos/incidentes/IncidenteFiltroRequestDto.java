package pe.gob.minedu.sigece.dtos.incidentes;

import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.enums.Gravedad;

import java.time.LocalDate;

public record IncidenteFiltroRequestDto(
        String termino,
        EstadoIncidente estado,
        Gravedad gravedad,
        Long tipoIncidenciaId,
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        Boolean reincidencia,
        Integer page,
        Integer size
) {
}