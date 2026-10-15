package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.seguimiento.*;
import pe.gob.minedu.sigece.enums.EstadoAccion;
import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.exceptions.OperacionInvalidaException;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.mappers.AccionSeguimientoMapper;
import pe.gob.minedu.sigece.models.AccionSeguimiento;
import pe.gob.minedu.sigece.models.Incidente;
import pe.gob.minedu.sigece.models.Usuario;
import pe.gob.minedu.sigece.persistence.repositories.IAccionSeguimientoRepository;
import pe.gob.minedu.sigece.persistence.repositories.IIncidenteRepository;
import pe.gob.minedu.sigece.persistence.repositories.IUsuarioRepository;
import pe.gob.minedu.sigece.persistence.specs.AccionSeguimientoSpecification;
import pe.gob.minedu.sigece.usecases.IAccionSeguimientoService;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccionSeguimientoServiceImpl implements IAccionSeguimientoService {

    private final IAccionSeguimientoRepository accionRepository;
    private final IIncidenteRepository incidenteRepository;
    private final IUsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public AccionSeguimientoResponseDto registrarAccion(UUID incidenteId, AccionSeguimientoRequestDto dto, Long usuarioId) {
        log.info("Registrando acción de seguimiento para incidente ID: {}", incidenteId);

        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));

        if (incidente.getEstado() == EstadoIncidente.CERRADO) {
            throw new OperacionInvalidaException(ErrorCatalog.INCIDENTE_CERRADO);
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        AccionSeguimiento accion = AccionSeguimientoMapper.toEntity(dto, incidente, usuario);

        if (incidente.getEstado() == EstadoIncidente.REGISTRADO || incidente.getEstado() == EstadoIncidente.EN_INVESTIGACION) {
            incidente.setEstado(EstadoIncidente.EN_SEGUIMIENTO);
            incidenteRepository.save(incidente);
        }

        return AccionSeguimientoMapper.toDto(accionRepository.save(accion));
    }

    @Override
    @Transactional
    public AccionSeguimientoResponseDto actualizarEstadoAccion(Long accionId, AccionEstadoRequestDto dto, Long usuarioId) {
        AccionSeguimiento accion = accionRepository.findById(accionId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.ACCION_SEGUIMIENTO_NOT_FOUND));

        accion.setEstadoAccion(dto.nuevoEstado());
        if (dto.nuevoEstado() == EstadoAccion.COMPLETADA) {
            accion.setFechaCompletada(OffsetDateTime.now());
        } else {
            accion.setFechaCompletada(null);
        }

        return AccionSeguimientoMapper.toDto(accionRepository.save(accion));
    }

    @Override
    @Transactional
    public AccionSeguimientoResponseDto reprogramarProximaRevision(Long accionId, AccionRevisionRequestDto dto, Long usuarioId) {
        AccionSeguimiento accion = accionRepository.findById(accionId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.ACCION_SEGUIMIENTO_NOT_FOUND));

        accion.setFechaProximaRevision(dto.nuevaFechaRevision());
        return AccionSeguimientoMapper.toDto(accionRepository.save(accion));
    }

    @Override
    @Transactional(readOnly = true)
    public AccionSeguimientoResponseDto obtenerPorId(Long accionId) {
        return accionRepository.findById(accionId)
                .map(AccionSeguimientoMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.ACCION_SEGUIMIENTO_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccionSeguimientoResponseDto> listarPorIncidente(UUID incidenteId) {
        return accionRepository.findByIncidenteIdOrderByFechaAccionDesc(incidenteId)
                .stream()
                .map(AccionSeguimientoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccionSeguimientoResponseDto> listarPendientesPorUsuario(Long usuarioId) {
        return accionRepository.findByUsuarioIdAndEstadoAccionNot(usuarioId, EstadoAccion.COMPLETADA)
                .stream()
                .map(AccionSeguimientoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccionSeguimientoResponseDto> listarAccionesVencidas() {
        return accionRepository.findAccionesVencidas(LocalDate.now())
                .stream()
                .map(AccionSeguimientoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean todasLasAccionesCompletadas(UUID incidenteId) {
        return accionRepository.countAccionesPendientesPorIncidente(incidenteId) == 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccionSeguimientoResponseDto> listarPorTipo(AccionFiltroRequestDto filtro) {
        return accionRepository.findAll(AccionSeguimientoSpecification.conFiltro(filtro))
                .stream()
                .map(AccionSeguimientoMapper::toDto)
                .toList();
    }
}