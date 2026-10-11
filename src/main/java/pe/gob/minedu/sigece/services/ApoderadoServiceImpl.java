package pe.gob.minedu.sigece.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoResponseDto;
import pe.gob.minedu.sigece.mappers.ApoderadoMapper;
import pe.gob.minedu.sigece.persistence.repositories.IApoderadoRepository;
import pe.gob.minedu.sigece.usecases.IApoderadoService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApoderadoServiceImpl implements IApoderadoService {

    private final IApoderadoRepository apoderadoRepository;

    @Override
    @Transactional
    public ApoderadoResponseDto registrarApoderado(ApoderadoRequestDto dto) {
        log.info("Registrando apoderado con número de documento: {}", dto.numeroDocumento());

        return Optional.of(dto)
                .filter(d -> !apoderadoRepository.existsByNumeroDocumento(d.numeroDocumento()))
                .map(ApoderadoMapper::toEntity)
                .map(apoderadoRepository::save)
                .map(ApoderadoMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Ya existe un apoderado registrado con el documento: " + dto.numeroDocumento()));
    }

    @Override
    @Transactional
    public ApoderadoResponseDto actualizarDatosContacto(Long apoderadoId, ApoderadoRequestDto dto) {
        log.info("Actualizando datos de contacto del apoderado ID: {}", apoderadoId);

        return apoderadoRepository.findById(apoderadoId)
                .map(apoderado -> {
                    apoderado.setTelefono(dto.telefono());
                    apoderado.setCorreo(dto.correo());
                    return apoderadoRepository.save(apoderado);
                })
                .map(ApoderadoMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Apoderado no encontrado con ID: " + apoderadoId));
    }

    @Override
    @Transactional(readOnly = true)
    public ApoderadoResponseDto obtenerPorId(Long apoderadoId) {
        return apoderadoRepository.findById(apoderadoId)
                .map(ApoderadoMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Apoderado no encontrado con ID: " + apoderadoId));
    }

    @Override
    @Transactional(readOnly = true)
    public ApoderadoResponseDto obtenerPorNumeroDocumento(String numeroDocumento) {
        return apoderadoRepository.findByNumeroDocumento(numeroDocumento)
                .map(ApoderadoMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Apoderado no encontrado con número de documento: " + numeroDocumento));
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
    @Transactional
    public void marcarContactoPrincipal(Long estudianteId, Long apoderadoId) {
        log.info("Marcando apoderado ID: {} como contacto principal para estudiante ID: {}", apoderadoId, estudianteId);

        if (!apoderadoRepository.existsById(apoderadoId)) {
            throw new EntityNotFoundException("Apoderado no encontrado con ID: " + apoderadoId);
        }

        apoderadoRepository.desmarcarContactosPrincipales(estudianteId);

        int filasAfectadas = apoderadoRepository.marcarComoContactoPrincipal(estudianteId, apoderadoId);
        if (filasAfectadas == 0) {
            throw new EntityNotFoundException(
                    "No se encontró la relación entre el estudiante ID: " + estudianteId + " y el apoderado ID: " + apoderadoId);
        }
    }
}