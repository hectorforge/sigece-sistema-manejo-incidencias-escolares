package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.apoderados.AsignarApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.VinculoFamiliarResponseDto;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.common.PaginationMeta;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudiantePerfilResponseDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteRequestDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteResponseDto;
import pe.gob.minedu.sigece.exceptions.OperacionInvalidaException;
import pe.gob.minedu.sigece.exceptions.RecursoDuplicadoException;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.mappers.ApoderadoMapper;
import pe.gob.minedu.sigece.mappers.EstudianteMapper;
import pe.gob.minedu.sigece.mappers.IncidenteMapper;
import pe.gob.minedu.sigece.models.Estudiante;
import pe.gob.minedu.sigece.models.EstudianteApoderado;
import pe.gob.minedu.sigece.persistence.repositories.IApoderadoRepository;
import pe.gob.minedu.sigece.persistence.repositories.IEstudianteApoderadoRepository;
import pe.gob.minedu.sigece.persistence.repositories.IEstudianteRepository;
import pe.gob.minedu.sigece.persistence.repositories.IIncidenteRepository;
import pe.gob.minedu.sigece.persistence.specs.EstudianteSpecification;
import pe.gob.minedu.sigece.usecases.IEstudianteService;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl implements IEstudianteService {

    private final IEstudianteRepository estudianteRepository;
    private final IApoderadoRepository apoderadoRepository;
    private final IEstudianteApoderadoRepository estudianteApoderadoRepository;
    private final IIncidenteRepository incidenteRepository;

    @Override
    @Transactional
    public EstudianteResponseDto registrarEstudiante(EstudianteRequestDto dto) {
        log.info("Registrando estudiante con código SIAGIE: {}", dto.codigoSiagie());

        if (dto.fechaNacimiento().isAfter(LocalDate.now())) {
            throw new OperacionInvalidaException(ErrorCatalog.ESTUDIANTE_FECHA_NACIMIENTO_INVALIDA);
        }

        return Optional.of(dto)
                .filter(d -> !estudianteRepository.existsByCodigoSiagie(d.codigoSiagie()))
                .map(EstudianteMapper::toEntity)
                .map(estudianteRepository::save)
                .map(EstudianteMapper::toDto)
                .orElseThrow(() -> new RecursoDuplicadoException(
                        ErrorCatalog.ESTUDIANTE_SIAGIE_DUPLICADO,
                        "El código SIAGIE " + dto.codigoSiagie() + " ya está registrado"));
    }

    @Override
    @Transactional
    public EstudianteResponseDto actualizarDatos(Long estudianteId, EstudianteRequestDto dto) {
        log.info("Actualizando estudiante ID: {}", estudianteId);

        return estudianteRepository.findById(estudianteId)
                .map(e -> {
                    e.setNombres(dto.nombres().trim());
                    e.setApellidos(dto.apellidos().trim());
                    e.setFechaNacimiento(dto.fechaNacimiento());
                    e.setNivel(dto.nivel());
                    e.setGrado(dto.grado().trim().toUpperCase());
                    e.setSeccion(dto.seccion().trim().toUpperCase());
                    return estudianteRepository.save(e);
                })
                .map(EstudianteMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.ESTUDIANTE_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteResponseDto obtenerPorId(Long estudianteId) {
        return estudianteRepository.findById(estudianteId)
                .map(EstudianteMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.ESTUDIANTE_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteResponseDto obtenerPorCodigoSiagie(String codigoSiagie) {
        return estudianteRepository.findByCodigoSiagie(codigoSiagie)
                .map(EstudianteMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.ESTUDIANTE_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public EstudiantePerfilResponseDto obtenerPerfil(Long estudianteId) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.ESTUDIANTE_NOT_FOUND));

        var apoderados = estudianteApoderadoRepository.findByEstudianteId(estudianteId)
                .stream()
                .map(ApoderadoMapper::toVinculoDto)
                .toList();

        var incidentes = incidenteRepository.findAllByEstudianteId(estudianteId)
                .stream()
                .map(IncidenteMapper::toResumenDto)
                .toList();

        return new EstudiantePerfilResponseDto(
                EstudianteMapper.toDto(estudiante),
                apoderados,
                incidentes.size(),
                incidentes
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EstudianteResponseDto> buscar(
            EstudianteFiltroRequestDto filtro
    ) {
        int pageNumber = Optional.ofNullable(filtro)
                .map(EstudianteFiltroRequestDto::page)
                .orElse(0);

        int pageSize = Optional.ofNullable(filtro)
                .map(EstudianteFiltroRequestDto::size)
                .orElse(10);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        var page = estudianteRepository.findAll(
                EstudianteSpecification.conFiltro(filtro),
                pageable
        );

        var content = page.map(EstudianteMapper::toDto)
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
    public void desactivarEstudiante(Long estudianteId) {
        estudianteRepository.findById(estudianteId)
                .ifPresentOrElse(e -> {
                    e.setActivo(false);
                    estudianteRepository.save(e);
                }, () -> { throw new RecursoNoEncontradoException(ErrorCatalog.ESTUDIANTE_NOT_FOUND); });
    }

    @Override
    @Transactional
    public void reactivarEstudiante(Long estudianteId) {
        estudianteRepository.findById(estudianteId)
                .ifPresentOrElse(e -> {
                    e.setActivo(true);
                    estudianteRepository.save(e);
                }, () -> { throw new RecursoNoEncontradoException(ErrorCatalog.ESTUDIANTE_NOT_FOUND); });
    }

    @Override
    @Transactional
    public VinculoFamiliarResponseDto asignarApoderado(Long estudianteId, AsignarApoderadoRequestDto dto) {
        log.info("Vinculando apoderado ID: {} con estudiante ID: {}", dto.apoderadoId(), estudianteId);

        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.ESTUDIANTE_NOT_FOUND));

        var apoderado = apoderadoRepository.findById(dto.apoderadoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.APODERADO_NOT_FOUND));

        if (Boolean.TRUE.equals(dto.esContactoPrincipal())) {
            estudianteApoderadoRepository.desmarcarContactosPrincipales(estudianteId);
        }

        EstudianteApoderado vinculo = estudianteApoderadoRepository.findByEstudianteIdAndApoderadoId(estudianteId, dto.apoderadoId())
                .orElseGet(() -> {
                    EstudianteApoderado nuevo = new EstudianteApoderado();
                    nuevo.setEstudiante(estudiante);
                    nuevo.setApoderado(apoderado);
                    return nuevo;
                });

        vinculo.setParentesco(dto.parentesco());
        vinculo.setEsContactoPrincipal(dto.esContactoPrincipal());

        return ApoderadoMapper.toVinculoDto(estudianteApoderadoRepository.save(vinculo));
    }

    @Override
    @Transactional
    public void desasignarApoderado(Long estudianteId, Long apoderadoId) {
        estudianteApoderadoRepository.deleteByEstudianteIdAndApoderadoId(estudianteId, apoderadoId);
    }

    @Override
    @Transactional(readOnly = true)
    public int contarIncidentesAsociados(Long estudianteId) {
        return estudianteRepository.countIncidentesByEstudianteId(estudianteId);
    }
}