package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.dashboard.DashboardFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.dashboard.DashboardResponseDto;
import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.enums.Gravedad;
import pe.gob.minedu.sigece.persistence.repositories.IIncidenteRepository;
import pe.gob.minedu.sigece.usecases.IDashboardService;

import java.util.EnumMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements IDashboardService {

    private final IIncidenteRepository incidenteRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponseDto obtenerIndicadores(DashboardFiltroRequestDto filtro) {
        long total = incidenteRepository.count();
        long cerrados = incidenteRepository.countByEstado(EstadoIncidente.CERRADO);
        long derivados = incidenteRepository.countByReportadoSiseveTrue();

        double porcentajeResolucion = total > 0 ? ((double) cerrados / total) * 100.0 : 0.0;

        Map<Gravedad, Long> porGravedad = new EnumMap<>(Gravedad.class);
        for (Gravedad g : Gravedad.values()) {
            porGravedad.put(g, 0L);
        }

        Map<EstadoIncidente, Long> porEstado = new EnumMap<>(EstadoIncidente.class);
        for (EstadoIncidente e : EstadoIncidente.values()) {
            porEstado.put(e, incidenteRepository.countByEstado(e));
        }

        return new DashboardResponseDto(
                total,
                derivados,
                cerrados,
                Math.round(porcentajeResolucion * 10.0) / 10.0,
                porGravedad,
                porEstado,
                Map.of(),
                Map.of()
        );
    }
}