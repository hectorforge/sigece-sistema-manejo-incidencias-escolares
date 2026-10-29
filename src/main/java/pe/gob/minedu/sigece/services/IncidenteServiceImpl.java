package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.common.PaginationMeta;
import pe.gob.minedu.sigece.dtos.incidentes.*;
import pe.gob.minedu.sigece.enums.EstadoDerivacion;
import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.enums.MedioOcurrencia;
import pe.gob.minedu.sigece.enums.RolNombre;
import pe.gob.minedu.sigece.exceptions.OperacionInvalidaException;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.mappers.*;
import pe.gob.minedu.sigece.models.*;
import pe.gob.minedu.sigece.persistence.repositories.*;
import pe.gob.minedu.sigece.persistence.specs.IncidenteSpecification;
import pe.gob.minedu.sigece.usecases.IAccesoAulaService;
import pe.gob.minedu.sigece.usecases.IIncidenteService;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidenteServiceImpl implements IIncidenteService {

    private final IIncidenteRepository incidenteRepository;
    private final ITipoIncidenciaRepository tipoIncidenciaRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ITutorAulaRepository tutorAulaRepository;
    private final IIncidenteInvolucradoRepository involucradoRepository;
    private final IAccionSeguimientoRepository accionSeguimientoRepository;
    private final IIncidenteAdjuntoRepository adjuntoRepository;
    private final INotificacionRepository notificacionRepository;
    private final IAccesoAulaService accesoAulaService; // Añadido para mantener la seguridad por aulas

    // ------------------------------------------------------------------ registro

    @Override
    @Transactional
    public IncidenteResponseDto registrarIncidente(IncidenteRequestDto dto, Long usuarioReportaId) {
        Usuario reportador = obtenerUsuarioActivo(usuarioReportaId);
        exigirRol(reportador, RolNombre.ROLE_TUTOR, RolNombre.ROLE_PSICOLOGO);

        if (dto.fechaOcurrencia().isAfter(OffsetDateTime.now())) {
            throw new OperacionInvalidaException(ErrorCatalog.BAD_REQUEST,
                    "La fecha de ocurrencia no puede ser futura");
        }

        TipoIncidencia tipo = tipoIncidenciaRepository.findById(dto.tipoIncidenciaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.TIPO_INCIDENCIA_NOT_FOUND));
        if (!tipo.getActivo()) {
            throw new OperacionInvalidaException(ErrorCatalog.TIPO_INCIDENCIA_INACTIVO);
        }

        // responsable_actual_id es NOT NULL: por defecto, quien reporta o el indicado
        Usuario responsable = (dto.responsableId() == null || dto.responsableId().equals(reportador.getId()))
                ? reportador
                : usuarioRepository.findById(dto.responsableId())
                .filter(Usuario::getActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_RESPONSABLE_NOT_FOUND));

        Incidente i = new Incidente();
        i.setCodigoCaso(generarCodigoCaso());
        i.setTipoIncidencia(tipo);
        i.setGravedad(dto.gravedad());
        i.setDescripcion(dto.descripcion().trim());
        i.setLugarOcurrencia(dto.lugarOcurrencia().trim());
        i.setMedioOcurrencia(dto.medioOcurrencia() != null ? dto.medioOcurrencia() : MedioOcurrencia.PRESENCIAL);
        i.setFechaOcurrencia(dto.fechaOcurrencia());
        i.setEstado(EstadoIncidente.REGISTRADO);
        i.setReportadoPor(reportador);
        i.setResponsableActual(responsable);
        i.setNotificarApoderado(Boolean.TRUE.equals(dto.notificarApoderado()));

        log.info("Incidente {} registrado por usuario ID: {}", i.getCodigoCaso(), usuarioReportaId);
        return IncidenteMapper.toDto(incidenteRepository.save(i));
    }

    // ------------------------------------------------------------------ consulta

    @Override
    @Transactional(readOnly = true)
    public IncidenteExpedienteResponseDto obtenerExpedienteCompleto(UUID incidenteId, Long usuarioId) {
        Usuario usuario = obtenerUsuarioActivo(usuarioId);
        Incidente incidente = buscarVisible(incidenteId, usuario);

        return new IncidenteExpedienteResponseDto(
                IncidenteMapper.toDto(incidente),
                involucradoRepository.findByIncidenteId(incidenteId).stream()
                        .map(InvolucradoMapper::toDto).toList(),
                accionSeguimientoRepository.findByIncidenteIdOrderByFechaAccionDesc(incidenteId).stream()
                        .map(AccionSeguimientoMapper::toDto).toList(),
                adjuntoRepository.findByIncidenteIdAndActivoTrueOrderByCreatedAtDesc(incidenteId).stream()
                        .map(AdjuntoMapper::toDto).toList(),
                notificacionRepository.findByIncidenteIdOrderByCreatedAtDesc(incidenteId).stream()
                        .map(NotificacionMapper::toDto).toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<IncidenteResumenResponseDto> buscarIncidentes(
            IncidenteFiltroRequestDto filtro,
            Long usuarioId
    ) {
        Usuario usuario = obtenerUsuarioActivo(usuarioId);

        int pageNumber = filtro != null && filtro.page() != null ? Math.max(filtro.page(), 0) : 0;
        int pageSize = filtro != null && filtro.size() != null ? Math.min(Math.max(filtro.size(), 1), 100) : 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "fechaRegistro"));

        var page = incidenteRepository.findAll(
                IncidenteSpecification.conFiltro(filtro, accesoAulaService.aulasRestringidas(usuario)),
                pageable
        );

        var incidentes = page.getContent();

        var incidenteIds = incidentes.stream()
                .map(Incidente::getId)
                .toList();

        var involucrados = incidenteIds.isEmpty()
                ? List.<IncidenteInvolucrado>of()
                : involucradoRepository
                .findByIncidenteIdInAndEstudianteIsNotNull(incidenteIds);

        Map<UUID, List<String>> estudiantesPorIncidente = involucrados.stream()
                .filter(inv -> inv.getEstudiante() != null)
                .collect(Collectors.groupingBy(
                        inv -> inv.getIncidente().getId(),
                        Collectors.mapping(
                                inv -> inv.getEstudiante().getNombres()
                                        + " " + inv.getEstudiante().getApellidos(),
                                Collectors.toList()
                        )
                ));

        var meta = new PaginationMeta(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        var contenido = incidentes.stream()
                .map(i -> new IncidenteResumenResponseDto(
                        i.getId(),
                        i.getCodigoCaso(),
                        i.getTipoIncidencia().getNombre(),
                        i.getGravedad(),
                        i.getLugarOcurrencia(),
                        i.getFechaOcurrencia(),
                        i.getEstado(),
                        i.getResponsableActual() == null
                                ? null
                                : i.getResponsableActual().getNombres()
                                + " " + i.getResponsableActual().getApellidos(),
                        estudiantesPorIncidente.getOrDefault(i.getId(), List.of())
                ))
                .toList();

        return new PagedResponse<>(contenido, meta);
    }

    // ------------------------------------------------------------------ gestión

    @Override
    @Transactional
    public IncidenteResponseDto clasificarGravedad(UUID incidenteId, ClasificarGravedadRequestDto dto, Long usuarioId) {
        exigirRol(obtenerUsuarioActivo(usuarioId), RolNombre.ROLE_ADMINISTRADOR, RolNombre.ROLE_PSICOLOGO);
        Incidente i = buscar(incidenteId);
        exigirNoCerrado(i);

        log.info("Gravedad de {} cambia de {} a {}. Justificación: {}",
                i.getCodigoCaso(), i.getGravedad(), dto.gravedad(), dto.justificacion());
        i.setGravedad(dto.gravedad());
        return IncidenteMapper.toDto(incidenteRepository.save(i));
    }

    @Override
    @Transactional
    public IncidenteResponseDto asignarResponsable(UUID incidenteId, AsignarResponsableRequestDto dto, Long usuarioQueAsignaId) {
        exigirRol(obtenerUsuarioActivo(usuarioQueAsignaId), RolNombre.ROLE_ADMINISTRADOR, RolNombre.ROLE_PSICOLOGO);

        Usuario responsable = usuarioRepository.findById(dto.responsableId())
                .filter(Usuario::getActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_RESPONSABLE_NOT_FOUND));

        Incidente i = buscar(incidenteId);
        exigirNoCerrado(i);

        i.setResponsableActual(responsable);
        if (i.getEstado() == EstadoIncidente.REGISTRADO) {
            i.setEstado(EstadoIncidente.EN_INVESTIGACION);
        }
        return IncidenteMapper.toDto(incidenteRepository.save(i));
    }

    @Override
    @Transactional
    public IncidenteResponseDto cambiarEstado(UUID incidenteId, CambiarEstadoRequestDto dto, Long usuarioId) {
        exigirRol(obtenerUsuarioActivo(usuarioId), RolNombre.ROLE_ADMINISTRADOR, RolNombre.ROLE_PSICOLOGO);
        Incidente i = buscar(incidenteId);

        if (dto.nuevoEstado() == EstadoIncidente.RESUELTO) {
            throw new OperacionInvalidaException(ErrorCatalog.INCIDENTE_TRANSICION_ESTADO_NO_PERMITIDA,
                    "Para resolver el caso use POST /incidentes/{id}/resolucion");
        }
        if (dto.nuevoEstado() == EstadoIncidente.CERRADO) {
            throw new OperacionInvalidaException(ErrorCatalog.INCIDENTE_TRANSICION_ESTADO_NO_PERMITIDA,
                    "Para cerrar el caso use POST /incidentes/{id}/cierre");
        }
        if (!transiciones(i.getEstado()).contains(dto.nuevoEstado())) {
            throw new OperacionInvalidaException(ErrorCatalog.INCIDENTE_TRANSICION_ESTADO_NO_PERMITIDA);
        }

        if (i.getEstado() == EstadoIncidente.RESUELTO) { // reapertura: se limpia la resolución
            i.setFechaResolucion(null);
            i.setResueltoPor(null);
        }

        log.info("Caso {}: {} -> {}. Motivo: {}", i.getCodigoCaso(), i.getEstado(), dto.nuevoEstado(), dto.motivo());
        i.setEstado(dto.nuevoEstado());
        return IncidenteMapper.toDto(incidenteRepository.save(i));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoIncidente> transicionesPermitidas(UUID incidenteId) {
        return transiciones(buscar(incidenteId).getEstado());
    }

    private List<EstadoIncidente> transiciones(EstadoIncidente estado) {
        return switch (estado) {
            case REGISTRADO -> List.of(EstadoIncidente.EN_INVESTIGACION);
            case EN_INVESTIGACION -> List.of(EstadoIncidente.EN_SEGUIMIENTO);
            case EN_SEGUIMIENTO -> List.of(EstadoIncidente.RESUELTO);
            case RESUELTO -> List.of(EstadoIncidente.CERRADO, EstadoIncidente.EN_SEGUIMIENTO);
            case CERRADO -> List.of();
        };
    }

    // ------------------------------------------------------------------ resolución y cierre

    @Override
    @Transactional
    public IncidenteResponseDto resolverIncidente(UUID incidenteId, ResolverIncidenteRequestDto dto, Long usuarioId) {
        Usuario psicologo = obtenerUsuarioActivo(usuarioId);
        exigirRol(psicologo, RolNombre.ROLE_PSICOLOGO);

        Incidente i = buscar(incidenteId);
        if (i.getEstado() != EstadoIncidente.EN_SEGUIMIENTO) {
            throw new OperacionInvalidaException(ErrorCatalog.INCIDENTE_TRANSICION_ESTADO_NO_PERMITIDA,
                    "Solo se puede resolver un caso en estado EN_SEGUIMIENTO");
        }

        i.setEstado(EstadoIncidente.RESUELTO);
        i.setFechaResolucion(OffsetDateTime.now());
        i.setResueltoPor(psicologo);
        if (dto != null && dto.observaciones() != null) {
            log.info("Caso {} resuelto. Observaciones: {}", i.getCodigoCaso(), dto.observaciones());
        }
        return IncidenteMapper.toDto(incidenteRepository.save(i));
    }

    @Override
    @Transactional(readOnly = true)
    public CierreChecklistResponseDto evaluarRequisitosCierre(UUID incidenteId) {
        Incidente i = buscar(incidenteId);

        boolean resuelto = i.getEstado() == EstadoIncidente.RESUELTO;
        boolean sinPendientes = accionSeguimientoRepository.countAccionesPendientesPorIncidente(incidenteId) == 0;
        boolean notificados = !Boolean.TRUE.equals(i.getNotificarApoderado())
                || notificacionRepository.countNotificacionesEfectivasApoderadosPrincipales(incidenteId) > 0;

        List<String> pendientes = new ArrayList<>();
        if (!resuelto) pendientes.add("El caso debe estar en estado RESUELTO");
        if (!sinPendientes) pendientes.add("Existen acciones de seguimiento pendientes");
        if (!notificados) pendientes.add("Falta notificar al apoderado");

        return new CierreChecklistResponseDto(
                sinPendientes,
                notificados,
                i.getActaCierre() != null,
                true,
                resuelto && sinPendientes && notificados,
                pendientes
        );
    }

    @Override
    @Transactional
    public IncidenteResponseDto cerrarCaso(UUID incidenteId, CerrarCasoRequestDto dto, Long usuarioId) {
        Usuario admin = obtenerUsuarioActivo(usuarioId);
        exigirRol(admin, RolNombre.ROLE_ADMINISTRADOR);

        Incidente i = buscar(incidenteId);
        exigirNoCerrado(i);
        if (i.getEstado() != EstadoIncidente.RESUELTO) {
            throw new OperacionInvalidaException(ErrorCatalog.INCIDENTE_TRANSICION_ESTADO_NO_PERMITIDA,
                    "Solo se puede cerrar un caso en estado RESUELTO");
        }

        CierreChecklistResponseDto checklist = evaluarRequisitosCierre(incidenteId);
        if (!checklist.listoParaCierre()) {
            throw new OperacionInvalidaException(ErrorCatalog.OPERACION_NO_PERMITIDA,
                    String.join("; ", checklist.observacionesPendientes()));
        }

        String acta = dto.actaCierre().trim();
        if (acta.length() < 10) {
            throw new OperacionInvalidaException(ErrorCatalog.BAD_REQUEST,
                    "El acta de cierre debe tener al menos 10 caracteres");
        }

        i.setActaCierre(acta);
        i.setConclusionesFinales(dto.conclusionesFinales().trim());
        i.setFechaCierre(OffsetDateTime.now());
        i.setCerradoPor(admin);
        i.setEstado(EstadoIncidente.CERRADO);

        return IncidenteMapper.toDto(incidenteRepository.save(i));
    }

    // ------------------------------------------------------------------ SíSeVe (registro manual)

    @Override
    @Transactional
    public IncidenteResponseDto registrarDerivacionSiseve(UUID incidenteId, DerivacionSiseveRequestDto dto, Long usuarioId) {
        Usuario admin = obtenerUsuarioActivo(usuarioId);
        exigirRol(admin, RolNombre.ROLE_ADMINISTRADOR);

        Incidente i = buscar(incidenteId);
        if (Boolean.TRUE.equals(i.getReportadoSiseve())) {
            throw new OperacionInvalidaException(ErrorCatalog.SISEVE_DERIVACION_INVALIDA,
                    "El caso ya fue derivado a SíSeVe (código " + i.getCodigoSiseve() + ")");
        }

        i.setReportadoSiseve(true);
        i.setCodigoSiseve(dto.codigoSiseve().trim());
        i.setFechaReporteSiseve(dto.fechaReporteSiseve());
        i.setObservacionesSiseve(dto.observacionesSiseve() != null ? dto.observacionesSiseve().trim() : null);
        i.setDerivadoPor(admin);
        return IncidenteMapper.toDto(incidenteRepository.save(i));
    }

    // ------------------------------------------------------------------ otros

    @Override
    @Transactional(readOnly = true)
    public List<CasoPendienteResponseDto> listarCasosPendientesDeAtencion(int diasUmbral) {
        OffsetDateTime ahora = OffsetDateTime.now();
        return incidenteRepository.findCasosDemorados(ahora.minusDays(diasUmbral)).stream()
                .map(i -> new CasoPendienteResponseDto(
                        i.getId(),
                        i.getCodigoCaso(),
                        i.getGravedad(),
                        i.getEstado(),
                        i.getFechaRegistro(),
                        Duration.between(i.getFechaRegistro(), ahora).toHours(),
                        i.getResponsableActual() != null
                                ? i.getResponsableActual().getNombres() + " " + i.getResponsableActual().getApellidos()
                                : "Sin Asignar"
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
    @Transactional(readOnly = true)
    public List<IncidenteResumenResponseDto> listarTodos() {
        return incidenteRepository.findAll()
                .stream()
                .map(IncidenteMapper::toResumenDto)
                .toList();
    }

    @Override
    @Transactional
    public String generarCodigoCaso() {
        int anio = Year.now().getValue();
        return String.format("INC-%d-%06d", anio, incidenteRepository.siguienteCorrelativo(anio));
    }

    // ------------------------------------------------------------------ helpers

    private Incidente buscar(UUID id) {
        return incidenteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));
    }

    private Incidente buscarVisible(UUID id, Usuario usuario) {
        List<TutorAula> aulas = accesoAulaService.aulasRestringidas(usuario);
        if (aulas == null) {
            return buscar(id);
        }
        Specification<Incidente> spec = IncidenteSpecification.conFiltro(null, aulas)
                .and((root, q, cb) -> cb.equal(root.get("id"), id));
        return incidenteRepository.findOne(spec)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));
    }

    private Usuario obtenerUsuarioActivo(Long id) {
        return usuarioRepository.findById(id)
                .filter(Usuario::getActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));
    }

    private void exigirRol(Usuario u, RolNombre... permitidos) {
        if (!Arrays.asList(permitidos).contains(u.getRol().getNombre())) {
            throw new OperacionInvalidaException(ErrorCatalog.ACCESS_DENIED);
        }
    }

    private void exigirNoCerrado(Incidente i) {
        if (i.getEstado() == EstadoIncidente.CERRADO) {
            throw new OperacionInvalidaException(ErrorCatalog.INCIDENTE_CERRADO);
        }
    }
}
