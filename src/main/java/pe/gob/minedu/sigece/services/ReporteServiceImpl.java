package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.reportes.*;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.models.Usuario;
import pe.gob.minedu.sigece.persistence.repositories.IIncidenteRepository;
import pe.gob.minedu.sigece.persistence.repositories.IUsuarioRepository;
import pe.gob.minedu.sigece.usecases.IReporteService;
import pe.gob.minedu.sigece.usecases.report.IReporteGeneratorPort;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements IReporteService {

    private final IIncidenteRepository incidenteRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IReporteGeneratorPort reporteGenerator;

    @Override
    public List<TipoReporteResponseDto> listarTiposReporte() {
        return List.of(
                new TipoReporteResponseDto("LIBRO_INCIDENCIAS", "Libro de Registro de Incidencias", "Formato oficial para inspecciones UGEL", List.of("PDF", "EXCEL")),
                new TipoReporteResponseDto("BALANCE_BIMESTRAL", "Balance Bimestral de Convivencia", "Métricas de casos atendidos y resueltos", List.of("PDF", "EXCEL")),
                new TipoReporteResponseDto("DERIVACIONES_SISEVE", "Reporte de Derivaciones SíSeVe", "Casos elevados a la plataforma del MINEDU", List.of("EXCEL"))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ReporteResponseDto generar(ReporteFiltroRequestDto filtro, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        var casos = incidenteRepository.findAll();

        List<Map<String, Object>> filas = casos.stream().map(i -> {
            Map<String, Object> fila = new HashMap<>();
            fila.put("codigoCaso", i.getCodigoCaso());
            fila.put("tipo", i.getTipoIncidencia().getNombre());
            fila.put("gravedad", i.getGravedad().name());
            fila.put("estado", i.getEstado().name());
            fila.put("fecha", i.getFechaOcurrencia().toString());
            return fila;
        }).toList();

        return new ReporteResponseDto(
                "Reporte Institucional de Convivencia Escolar",
                "IE SIGECE",
                OffsetDateTime.now(),
                usuario.getNombres() + " " + usuario.getApellidos(),
                filas.size(),
                filas
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ArchivoResponseDto exportar(ExportarReporteRequestDto dto, Long usuarioId) {
        log.info("Exportando reporte tipo: {} en formato: {}", dto.tipoReporte(), dto.formato());

        ReporteResponseDto preview = generar(dto.filtro(), usuarioId);

        Map<String, Object> params = new HashMap<>();
        params.put("TITULO", preview.titulo());
        params.put("GENERADO_POR", preview.generadoPor());
        params.put("ANONIMIZAR", dto.anonimizarDatosMenores());

        return reporteGenerator.exportarDocumento(dto, preview.filas(), params);
    }
}