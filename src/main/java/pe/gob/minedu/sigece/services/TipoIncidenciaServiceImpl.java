package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.tiposincidencia.TipoIncidenciaRequestDto;
import pe.gob.minedu.sigece.dtos.tiposincidencia.TipoIncidenciaResponseDto;
import pe.gob.minedu.sigece.enums.CategoriaSiseve;
import pe.gob.minedu.sigece.exceptions.RecursoDuplicadoException;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.mappers.TipoIncidenciaMapper;
import pe.gob.minedu.sigece.persistence.repositories.ITipoIncidenciaRepository;
import pe.gob.minedu.sigece.usecases.ITipoIncidenciaService;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TipoIncidenciaServiceImpl implements ITipoIncidenciaService {

    private final ITipoIncidenciaRepository tipoIncidenciaRepository;

    @Override
    @Transactional
    public TipoIncidenciaResponseDto crear(TipoIncidenciaRequestDto dto) {
        log.info("Creando tipo de incidencia: {}", dto.nombre());

        return Optional.of(dto)
                .filter(d -> !tipoIncidenciaRepository.existsByNombre(d.nombre()))
                .map(TipoIncidenciaMapper::toEntity)
                .map(tipoIncidenciaRepository::save)
                .map(TipoIncidenciaMapper::toDto)
                .orElseThrow(() -> new RecursoDuplicadoException(
                        ErrorCatalog.TIPO_INCIDENCIA_INACTIVO,
                        "El tipo de incidencia " + dto.nombre() + " ya está registrado"));
    }

    @Override
    @Transactional
    public TipoIncidenciaResponseDto actualizar(Long id, TipoIncidenciaRequestDto dto) {
        return tipoIncidenciaRepository.findById(id)
                .map(tipo -> {
                    tipo.setNombre(dto.nombre().trim());
                    tipo.setCategoriaSiseve(dto.categoriaSiseve());
                    tipo.setDescripcion(dto.descripcion() != null ? dto.descripcion().trim() : null);
                    return tipoIncidenciaRepository.save(tipo);
                })
                .map(TipoIncidenciaMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.TIPO_INCIDENCIA_NOT_FOUND));
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        tipoIncidenciaRepository.findById(id)
                .ifPresentOrElse(tipo -> {
                    tipo.setActivo(false);
                    tipoIncidenciaRepository.save(tipo);
                }, () -> { throw new RecursoNoEncontradoException(ErrorCatalog.TIPO_INCIDENCIA_NOT_FOUND); });
    }

    @Override
    @Transactional
    public void reactivar(Long id) {
        tipoIncidenciaRepository.findById(id)
                .ifPresentOrElse(tipo -> {
                    tipo.setActivo(true);
                    tipoIncidenciaRepository.save(tipo);
                }, () -> { throw new RecursoNoEncontradoException(ErrorCatalog.TIPO_INCIDENCIA_NOT_FOUND); });
    }

    @Override
    @Transactional(readOnly = true)
    public TipoIncidenciaResponseDto obtenerPorId(Long id) {
        return tipoIncidenciaRepository.findById(id)
                .map(TipoIncidenciaMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.TIPO_INCIDENCIA_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoIncidenciaResponseDto> listarTodos() {
        return tipoIncidenciaRepository.findAll()
                .stream()
                .map(TipoIncidenciaMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoIncidenciaResponseDto> listarActivos() {
        return tipoIncidenciaRepository.findAllByActivoTrue()
                .stream()
                .map(TipoIncidenciaMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoIncidenciaResponseDto> listarPorCategoriaSiseve(CategoriaSiseve categoria) {
        return tipoIncidenciaRepository.findAllByCategoriaSiseveAndActivoTrue(categoria)
                .stream()
                .map(TipoIncidenciaMapper::toDto)
                .toList();
    }
}