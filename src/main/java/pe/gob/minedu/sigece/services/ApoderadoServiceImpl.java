package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoResponseDto;
import pe.gob.minedu.sigece.dtos.apoderados.VinculoFamiliarResponseDto;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.common.PaginationMeta;
import pe.gob.minedu.sigece.exceptions.RecursoDuplicadoException;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.mappers.ApoderadoMapper;
import pe.gob.minedu.sigece.persistence.repositories.IApoderadoRepository;
import pe.gob.minedu.sigece.persistence.repositories.IEstudianteApoderadoRepository;
import pe.gob.minedu.sigece.persistence.repositories.IEstudianteRepository;
import pe.gob.minedu.sigece.persistence.specs.ApoderadoSpecification;
import pe.gob.minedu.sigece.usecases.IApoderadoService;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApoderadoServiceImpl implements IApoderadoService {

    private final IApoderadoRepository apoderadoRepository;
    private final IEstudianteRepository estudianteRepository;
    private final IEstudianteApoderadoRepository estudianteApoderadoRepository;

    @Override
    @Transactional
    public ApoderadoResponseDto registrarApoderado(ApoderadoRequestDto dto) {
        log.info("Registrando apoderado con documento: {}", dto.numeroDocumento());

        return Optional.of(dto)
                .filter(d -> !apoderadoRepository.existsByNumeroDocumento(d.numeroDocumento()))
                .map(ApoderadoMapper::toEntity)
                .map(apoderadoRepository::save)
                .map(ApoderadoMapper::toDto)
                .orElseThrow(() -> new RecursoDuplicadoException(
                        ErrorCatalog.APODERADO_DOCUMENTO_DUPLICADO,
                        "El documento " + dto.numeroDocumento() + " ya está registrado"));
    }

    @Override
    @Transactional
    public ApoderadoResponseDto actualizarDatosContacto(Long apoderadoId, ApoderadoRequestDto dto) {
        log.info("Actualizando contacto de apoderado ID: {}", apoderadoId);

        return apoderadoRepository.findById(apoderadoId)
                .map(apoderado -> {
                    apoderado.setTelefono(dto.telefono().trim());
                    apoderado.setCorreo(dto.correo() != null ? dto.correo().trim().toLowerCase() : null);
                    return apoderadoRepository.save(apoderado);
                })
                .map(ApoderadoMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.APODERADO_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public ApoderadoResponseDto obtenerPorId(Long apoderadoId) {
        return apoderadoRepository.findById(apoderadoId)
                .map(ApoderadoMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.APODERADO_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public ApoderadoResponseDto obtenerPorNumeroDocumento(String numeroDocumento) {
        return apoderadoRepository.findByNumeroDocumento(numeroDocumento)
                .map(ApoderadoMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.APODERADO_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApoderadoResponseDto> listarPorEstudiante(Long estudianteId) {
        return apoderadoRepository.findAllByEstudianteId(estudianteId)
                .stream()
                .map(ApoderadoMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VinculoFamiliarResponseDto> listarVinculosPorApoderado(Long apoderadoId) {
        return Optional.of(apoderadoId)
                .filter(apoderadoRepository::existsById)
                .map(estudianteApoderadoRepository::findByApoderadoId)
                .map(lista -> lista.stream().map(ApoderadoMapper::toVinculoDto).toList())
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.APODERADO_NOT_FOUND));
    }

    @Override
    @Transactional
    public void marcarContactoPrincipal(Long estudianteId, Long apoderadoId) {
        log.info("Marcando principal al apoderado ID: {} para estudiante ID: {}", apoderadoId, estudianteId);

        if (!estudianteRepository.existsById(estudianteId)) {
            throw new RecursoNoEncontradoException(ErrorCatalog.ESTUDIANTE_NOT_FOUND);
        }
        if (!apoderadoRepository.existsById(apoderadoId)) {
            throw new RecursoNoEncontradoException(ErrorCatalog.APODERADO_NOT_FOUND);
        }

        estudianteApoderadoRepository.desmarcarContactosPrincipales(estudianteId);

        int actualizados = estudianteApoderadoRepository.marcarComoContactoPrincipal(estudianteId, apoderadoId);
        if (actualizados == 0) {
            throw new RecursoNoEncontradoException(ErrorCatalog.RELACION_APODERADO_NOT_FOUND);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ApoderadoResponseDto> buscar(
            ApoderadoFiltroRequestDto filtro
    ) {
        int pageNumber = Optional.ofNullable(filtro)
                .map(ApoderadoFiltroRequestDto::page)
                .orElse(0);

        int pageSize = Optional.ofNullable(filtro)
                .map(ApoderadoFiltroRequestDto::size)
                .orElse(10);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        var page = apoderadoRepository.findAll(
                ApoderadoSpecification.conFiltro(filtro),
                pageable
        );

        var content = page.map(ApoderadoMapper::toDto)
                .getContent();

        var meta = new PaginationMeta(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return new PagedResponse<>(content, meta);
    }
}