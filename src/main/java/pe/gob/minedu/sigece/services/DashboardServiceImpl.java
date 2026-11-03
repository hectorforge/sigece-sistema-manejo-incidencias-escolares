package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.dashboard.DashboardFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.dashboard.DashboardResponseDto;
import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.enums.Gravedad;
import pe.gob.minedu.sigece.models.Incidente;
import pe.gob.minedu.sigece.persistence.repositories.IIncidenteRepository;
import pe.gob.minedu.sigece.persistence.specs.DashboardIncidenteSpecification;
import pe.gob.minedu.sigece.usecases.IDashboardService;

import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements IDashboardService {

    private static final DateTimeFormatter FORMATO_MES = DateTimeFormatter.ofPattern("yyyy-MM");
    private final IIncidenteRepository incidenteRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponseDto obtenerIndicadores(DashboardFiltroRequestDto filtro) {
        // 1. Obtener incidentes respetando fechas, nivel y grado
        Specification<Incidente> spec = DashboardIncidenteSpecification.conFiltro(filtro);
        List<Incidente> incidentes = incidenteRepository.findAll(spec);

        long total = incidentes.size();
        long cerrados = incidentes.stream()
                .filter(i -> i.getEstado() == EstadoIncidente.CERRADO)
                .count();

        // Corrección: Usar getReportadoSiseve() seguro con Boolean.TRUE
        long derivados = incidentes.stream()
                .filter(i -> Boolean.TRUE.equals(i.getReportadoSiseve()))
                .count();

        double porcentajeResolucion = total > 0 ? ((double) cerrados / total) * 100.0 : 0.0;

        // 2. Incidentes por Gravedad
        Map<Gravedad, Long> porGravedad = new EnumMap<>(Gravedad.class);
        for (Gravedad g : Gravedad.values()) {
            porGravedad.put(g, 0L);
        }
        incidentes.stream()
                .filter(i -> i.getGravedad() != null)
                .collect(Collectors.groupingBy(Incidente::getGravedad, Collectors.counting()))
                .forEach(porGravedad::put);

        // 3. Incidentes por Estado
        Map<EstadoIncidente, Long> porEstado = new EnumMap<>(EstadoIncidente.class);
        for (EstadoIncidente e : EstadoIncidente.values()) {
            porEstado.put(e, 0L);
        }
        incidentes.stream()
                .filter(i -> i.getEstado() != null)
                .collect(Collectors.groupingBy(Incidente::getEstado, Collectors.counting()))
                .forEach(porEstado::put);

        // 4. Incidentes por Tipo de Incidencia
        Map<String, Long> porTipo = incidentes.stream()
                .collect(Collectors.groupingBy(
                        i -> i.getTipoIncidencia() != null ? i.getTipoIncidencia().getNombre() : "SIN CLASIFICAR",
                        Collectors.counting()
                ));

        // 5. Incidentes por Mes ('yyyy-MM')
        Map<String, Long> porMes = incidentes.stream()
                .filter(i -> i.getFechaOcurrencia() != null)
                .collect(Collectors.groupingBy(
                        i -> i.getFechaOcurrencia().format(FORMATO_MES),
                        TreeMap::new,
                        Collectors.counting()
                ));

        return new DashboardResponseDto(
                total,
                derivados,
                cerrados,
                Math.round(porcentajeResolucion * 10.0) / 10.0,
                porGravedad,
                porEstado,
                porTipo,
                porMes
        );
    }
}