package pe.gob.minedu.sigece.dtos.dashboard;

import pe.gob.minedu.sigece.enums.Nivel;

import java.time.LocalDate;

public record DashboardFiltroRequestDto(
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        Nivel nivel,
        String grado
) {
}