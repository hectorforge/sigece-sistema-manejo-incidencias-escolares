package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.involucrados.*;
import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.enums.RolInvolucrado;
import pe.gob.minedu.sigece.exceptions.OperacionInvalidaException;
import pe.gob.minedu.sigece.exceptions.RecursoDuplicadoException;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.mappers.InvolucradoMapper;
import pe.gob.minedu.sigece.models.Estudiante;
import pe.gob.minedu.sigece.models.Incidente;
import pe.gob.minedu.sigece.models.IncidenteInvolucrado;
import pe.gob.minedu.sigece.models.Usuario;
import pe.gob.minedu.sigece.persistence.repositories.IEstudianteRepository;
import pe.gob.minedu.sigece.persistence.repositories.IIncidenteInvolucradoRepository;
import pe.gob.minedu.sigece.persistence.repositories.IIncidenteRepository;
import pe.gob.minedu.sigece.persistence.repositories.IUsuarioRepository;
import pe.gob.minedu.sigece.usecases.IIncidenteInvolucradoService;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidenteInvolucradoServiceImpl implements IIncidenteInvolucradoService {

    private final IIncidenteInvolucradoRepository involucradoRepository;
    private final IIncidenteRepository incidenteRepository;
    private final IEstudianteRepository estudianteRepository;
    private final IUsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public InvolucradoResponseDto agregarInvolucrado(UUID incidenteId, InvolucradoRequestDto dto, Long usuarioId) {
        log.info("Agregando involucrado a incidente ID: {}", incidenteId);

        boolean esEstudiante = dto.estudianteId() != null;
        boolean esUsuario = dto.usuarioId() != null;

        if (esEstudiante == esUsuario) {
            throw new OperacionInvalidaException(ErrorCatalog.INVOLUCRADO_ENTIDAD_INVALIDA);
        }

        Incidente incidente = incidenteRepository.findById(incidenteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INCIDENTE_NOT_FOUND));

        IncidenteInvolucrado ii = new IncidenteInvolucrado();
        ii.setIncidente(incidente);
        ii.setRolInvolucrado(dto.rolInvolucrado());
        ii.setObservaciones(dto.observaciones() != null ? dto.observaciones().trim() : null);

        if (esEstudiante) {
            if (involucradoRepository.existsByIncidenteIdAndEstudianteIdAndRolInvolucrado(incidenteId, dto.estudianteId(), dto.rolInvolucrado())) {
                throw new RecursoDuplicadoException(ErrorCatalog.INVOLUCRADO_DUPLICADO);
            }
            Estudiante estudiante = estudianteRepository.findById(dto.estudianteId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.ESTUDIANTE_NOT_FOUND));
            ii.setEstudiante(estudiante);
        } else {
            if (involucradoRepository.existsByIncidenteIdAndUsuarioIdAndRolInvolucrado(incidenteId, dto.usuarioId(), dto.rolInvolucrado())) {
                throw new RecursoDuplicadoException(ErrorCatalog.INVOLUCRADO_DUPLICADO);
            }
            Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));
            ii.setUsuario(usuario);
        }

        return InvolucradoMapper.toDto(involucradoRepository.save(ii));
    }

    @Override
    @Transactional
    public void removerInvolucrado(UUID incidenteId, Long involucradoId, RemoverInvolucradoRequestDto dto, Long usuarioId) {
        IncidenteInvolucrado ii = involucradoRepository.findById(involucradoId)
                .filter(x -> x.getIncidente().getId().equals(incidenteId))
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.INVOLUCRADO_NOT_FOUND));
        if (ii.getIncidente().getEstado() == EstadoIncidente.CERRADO) {
            throw new OperacionInvalidaException(ErrorCatalog.INCIDENTE_CERRADO);
        }
        log.info("Involucrado {} retirado del caso {} por usuario {}. Motivo: {}",
                involucradoId, incidenteId, usuarioId, dto.motivo());
        involucradoRepository.delete(ii);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvolucradoResponseDto> listarPorIncidente(UUID incidenteId) {
        return involucradoRepository.findByIncidenteId(incidenteId)
                .stream()
                .map(InvolucradoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InvolucradosAgrupadosResponseDto listarAgrupadosPorRol(UUID incidenteId) {
        var todos = involucradoRepository.findByIncidenteId(incidenteId);

        var victimas = todos.stream().filter(i -> i.getRolInvolucrado() == RolInvolucrado.VICTIMA).map(InvolucradoMapper::toDto).toList();
        var agresores = todos.stream().filter(i -> i.getRolInvolucrado() == RolInvolucrado.AGRESOR).map(InvolucradoMapper::toDto).toList();
        var testigos = todos.stream().filter(i -> i.getRolInvolucrado() == RolInvolucrado.TESTIGO).map(InvolucradoMapper::toDto).toList();

        return new InvolucradosAgrupadosResponseDto(victimas, agresores, testigos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvolucradoResponseDto> listarPorEstudianteYRol(Long estudianteId, RolInvolucrado rol) {
        return involucradoRepository.findByEstudianteIdAndRolInvolucrado(estudianteId, rol)
                .stream()
                .map(InvolucradoMapper::toDto)
                .toList();
    }
}