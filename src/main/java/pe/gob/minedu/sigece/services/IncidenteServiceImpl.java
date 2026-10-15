package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.common.PaginationMeta;
import pe.gob.minedu.sigece.dtos.incidentes.*;
import pe.gob.minedu.sigece.enums.EstadoDerivacion;
import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.exceptions.OperacionInvalidaException;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.mappers.IncidenteMapper;
import pe.gob.minedu.sigece.models.Incidente;
import pe.gob.minedu.sigece.models.TipoIncidencia;
import pe.gob.minedu.sigece.models.Usuario;
import pe.gob.minedu.sigece.persistence.repositories.*;
import pe.gob.minedu.sigece.persistence.specs.IncidenteSpecification;
import pe.gob.minedu.sigece.usecases.IIncidenteService;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.time.OffsetDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidenteServiceImpl implements IIncidenteService {

    private final IIncidenteRepository incidenteRepository;
    private final ITipoIncidenciaRepository tipoIncidenciaRepository;
    private final IUsuarioRepository usuarioRepository;
    private final IIncidenteInvolucradoRepository involucradoRepository;
    private final IAccionSeguimientoRepository accionSeguimientoRepository;
    private final IIncidenteAdjuntoRepository adjuntoRepository;
    private final INotificacionRepository notificacionRepository;

    @Override
    @Transactional
    public IncidenteResponseDto registrarIncidente(IncidenteRequestDto dto, Long usuarioReportaId) {
        log.info("Registrando incidente reportado por usuario ID: {}", usuarioReportaId);

        Usuario reportador = usuarioRepository.findById(usuarioReportaId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        TipoIncidencia tipo = tipoIncidenciaRepository.findById(dto.tipoIncidenciaId())
                .filter(TipoIncidencia::getActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.TIPO_INCIDENCIA_NOT_FOUND));

        Incidente incidente = new Incidente();
        incidente.setCodigoCaso(generarCodigoCaso());
        incidente.setTipoIncidencia(tipo);
        incidente.setGravedad(dto.gravedad());
        incidente.setDescripcion(dto.descripcion().trim());
        incidente.setLugarOcurrencia(dto.lugarOcurrencia().trim());
        incidente.setFechaOcurrencia(dto.fechaOcurrencia());
        incidente.setReportadoPor(reportador);
        incidente.setEstado(EstadoIncidente.REGISTRADO);
        incidente.setEstadoDerivacion(EstadoDerivacion.NO_DERIVADA);

        return IncidenteMapper.toDto(incidenteRepository.save(incidente));
    }

    @Override
    @Transactional(readOnly = true)
    public IncidenteExpedienteResponseDto obtenerExpedienteCompleto(UUID incidenteId, Long usuarioId) {
        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));

        return new IncidenteExpedienteResponseDto(
                IncidenteMapper.toDto(incidente),
                List.of(), // Se completan mediante servicios respectivos
                List.of(),
                List.of(),
                List.of()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<IncidenteResumenResponseDto> buscarIncidentes(
            IncidenteFiltroRequestDto filtro,
            Long usuarioId
    ) {
        int pageNumber = Optional.ofNullable(filtro)
                .map(IncidenteFiltroRequestDto::page)
                .orElse(0);

        int pageSize = Optional.ofNullable(filtro)
                .map(IncidenteFiltroRequestDto::size)
                .orElse(10);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        var page = incidenteRepository.findAll(
                IncidenteSpecification.conFiltro(filtro),
                pageable
        );

        var content = page
                .map(IncidenteMapper::toResumenDto)
                .getContent();

        var meta = new PaginationMeta(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return new PagedResponse<>(content, meta);
    }

    @Override
    @Transactional
    public IncidenteResponseDto clasificarGravedad(UUID incidenteId, ClasificarGravedadRequestDto dto, Long usuarioId) {
        return incidenteRepository.findById(incidenteId)
                .map(i -> {
                    i.setGravedad(dto.gravedad());
                    return incidenteRepository.save(i);
                })
                .map(IncidenteMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));
    }

    @Override
    @Transactional
    public IncidenteResponseDto asignarResponsable(UUID incidenteId, AsignarResponsableRequestDto dto, Long usuarioQueAsignaId) {
        Usuario responsable = usuarioRepository.findById(dto.responsableId())
                .filter(Usuario::getActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        return incidenteRepository.findById(incidenteId)
                .map(i -> {
                    i.setResponsableActual(responsable);
                    if (i.getEstado() == EstadoIncidente.REGISTRADO) {
                        i.setEstado(EstadoIncidente.EN_INVESTIGACION);
                    }
                    return incidenteRepository.save(i);
                })
                .map(IncidenteMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));
    }

    @Override
    @Transactional
    public IncidenteResponseDto cambiarEstado(UUID incidenteId, CambiarEstadoRequestDto dto, Long usuarioId) {
        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));

        if (!transicionesPermitidas(incidenteId).contains(dto.nuevoEstado())) {
            throw new OperacionInvalidaException(ErrorCatalog.INCIDENTE_TRANSICION_ESTADO_NO_PERMITIDA);
        }

        incidente.setEstado(dto.nuevoEstado());
        return IncidenteMapper.toDto(incidenteRepository.save(incidente));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoIncidente> transicionesPermitidas(UUID incidenteId) {
        return incidenteRepository.findById(incidenteId)
                .map(Incidente::getEstado)
                .map(estado -> switch (estado) {
                    case REGISTRADO -> List.of(EstadoIncidente.EN_INVESTIGACION, EstadoIncidente.DERIVADO_SISEVE);
                    case EN_INVESTIGACION -> List.of(EstadoIncidente.EN_SEGUIMIENTO, EstadoIncidente.DERIVADO_SISEVE);
                    case EN_SEGUIMIENTO -> List.of(EstadoIncidente.RESUELTO, EstadoIncidente.DERIVADO_SISEVE);
                    case RESUELTO -> List.of(EstadoIncidente.CERRADO, EstadoIncidente.EN_SEGUIMIENTO);
                    case DERIVADO_SISEVE -> List.of(EstadoIncidente.EN_SEGUIMIENTO, EstadoIncidente.CERRADO);
                    case CERRADO -> List.<EstadoIncidente>of();
                })
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public CierreChecklistResponseDto evaluarRequisitosCierre(UUID incidenteId) {
        boolean sinPendientes = accionSeguimientoRepository.countAccionesPendientesPorIncidente(incidenteId) == 0;
        boolean notificados = notificacionRepository.countNotificacionesEfectivasApoderadosPrincipales(incidenteId) > 0;

        return new CierreChecklistResponseDto(
                sinPendientes,
                notificados,
                true,
                true,
                sinPendientes && notificados,
                sinPendientes && notificados ? List.of() : List.of("Existen acciones pendientes o falta notificar apoderados")
        );
    }

    @Override
    @Transactional
    public IncidenteResponseDto cerrarCaso(UUID incidenteId, CerrarCasoRequestDto dto, Long usuarioId) {
        Usuario usuarioCierra = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));

        if (incidente.getEstado() == EstadoIncidente.CERRADO) {
            throw new OperacionInvalidaException(ErrorCatalog.INCIDENTE_CERRADO);
        }

        incidente.setActaCierre(dto.actaCierre());
        incidente.setFechaCierre(OffsetDateTime.now());
        incidente.setCerradoPor(usuarioCierra);
        incidente.setEstado(EstadoIncidente.CERRADO);

        return IncidenteMapper.toDto(incidenteRepository.save(incidente));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CasoPendienteResponseDto> listarCasosPendientesDeAtencion(int diasUmbral) {
        OffsetDateTime limite = OffsetDateTime.now().minusDays(diasUmbral);
        return incidenteRepository.findCasosDemorados(limite).stream()
                .map(i -> new CasoPendienteResponseDto(
                        i.getId(),
                        i.getCodigoCaso(),
                        i.getGravedad(),
                        i.getEstado(),
                        i.getFechaRegistro(),
                        java.time.Duration.between(i.getFechaRegistro(), OffsetDateTime.now()).toHours(),
                        i.getResponsableActual() != null ? i.getResponsableActual().getNombres() : "Sin Asignar"
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReincidenciaResponseDto evaluarReincidencia(ReincidenciaRequestDto dto) {
        OffsetDateTime ventana = Optional.ofNullable(dto.fechaReferencia())
                .orElseGet(OffsetDateTime::now)
                .minusDays(30);

        int conteo = incidenteRepository.countReincidenciasPorEstudiante(dto.estudianteId(), ventana);

        return new ReincidenciaResponseDto(
                dto.estudianteId(),
                conteo >= 3,
                conteo,
                3,
                30,
                List.of()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncidenteResumenResponseDto> obtenerHistorialPorEstudiante(Long estudianteId) {
        return incidenteRepository.findAllByEstudianteId(estudianteId).stream()
                .map(IncidenteMapper::toResumenDto)
                .toList();
    }

    @Override
    @Transactional
    public String generarCodigoCaso() {
        int anio = Year.now().getValue();
        // int correlativo = incidenteRepository.obtenerSiguienteCorrelativoAnual(anio);
        // TODO: Implementar metodo para generacio de correlativo
        //return nullString.format("INC-%d-%06d", anio, correlativo);
        return "INC";
    }
}