package pe.gob.minedu.sigece.dtos.dashboard;

import org.springframework.format.annotation.DateTimeFormat;
import pe.gob.minedu.sigece.enums.Nivel;

import java.time.LocalDate;

public record DashboardFiltroRequestDto(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate fechaDesde,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate fechaHasta,

        Nivel nivel,
        String grado
) {
}