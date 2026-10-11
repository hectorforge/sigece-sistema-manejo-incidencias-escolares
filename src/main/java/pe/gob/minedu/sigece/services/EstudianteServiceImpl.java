package pe.gob.minedu.sigece.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteRequestDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteResponseDto;
import pe.gob.minedu.sigece.enums.Nivel;
import pe.gob.minedu.sigece.mappers.EstudianteMapper;
import pe.gob.minedu.sigece.persistence.repositories.IEstudianteRepository;
import pe.gob.minedu.sigece.usecases.IEstudianteService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl implements IEstudianteService {

    private final IEstudianteRepository estudianteRepository;

    @Override
    @Transactional
    public EstudianteResponseDto registrarEstudiante(EstudianteRequestDto dto) {
        log.info("Registrando estudiante con código SIAGIE: {}", dto.codigoSiagie());

        return Optional.of(dto)
                .filter(d -> !estudianteRepository.existsByCodigoSiagie(d.codigoSiagie()))
                .map(EstudianteMapper::toEntity)
                .map(estudianteRepository::save)
                .map(EstudianteMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Ya existe un estudiante registrado con el código SIAGIE: " + dto.codigoSiagie()));
    }

    @Override
    @Transactional
    public EstudianteResponseDto actualizarDatos(Long estudianteId, EstudianteRequestDto dto) {
        log.info("Actualizando datos del estudiante con ID: {}", estudianteId);

        return estudianteRepository.findById(estudianteId)
                .map(estudiante -> {
                    estudiante.setNombres(dto.nombres());
                    estudiante.setApellidos(dto.apellidos());
                    estudiante.setFechaNacimiento(dto.fechaNacimiento());
                    estudiante.setNivel(dto.nivel());
                    estudiante.setGrado(dto.grado());
                    estudiante.setSeccion(dto.seccion());
                    return estudianteRepository.save(estudiante);
                })
                .map(EstudianteMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado con ID: " + estudianteId));
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteResponseDto obtenerPorId(Long estudianteId) {
        return estudianteRepository.findById(estudianteId)
                .map(EstudianteMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado con ID: " + estudianteId));
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteResponseDto obtenerPorCodigoSiagie(String codigoSiagie) {
        return estudianteRepository.findByCodigoSiagie(codigoSiagie)
                .map(EstudianteMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado con código SIAGIE: " + codigoSiagie));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstudianteResponseDto> listarPorGradoYSeccion(String grado, String seccion, Nivel nivel) {
        return estudianteRepository.findAllByGradoAndSeccionAndNivelAndActivoTrue(grado, seccion, nivel)
                .stream()
                .map(EstudianteMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void desactivarEstudiante(Long estudianteId) {
        log.info("Desactivando estudiante con ID: {}", estudianteId);

        estudianteRepository.findById(estudianteId)
                .ifPresentOrElse(estudiante -> {
                    estudiante.setActivo(false);
                    estudianteRepository.save(estudiante);
                }, () -> {
                    throw new EntityNotFoundException("Estudiante no encontrado con ID: " + estudianteId);
                });
    }

    @Override
    @Transactional
    public void asignarApoderado(Long estudianteId, Long apoderadoId, String parentesco, boolean esContactoPrincipal) {
        log.info("Asignando apoderado ID: {} al estudiante ID: {}", apoderadoId, estudianteId);

        if (!estudianteRepository.existsById(estudianteId)) {
            throw new EntityNotFoundException("Estudiante no encontrado con ID: " + estudianteId);
        }

        estudianteRepository.upsertRelacionApoderado(estudianteId, apoderadoId, parentesco, esContactoPrincipal);
    }

    @Override
    @Transactional(readOnly = true)
    public int contarIncidentesAsociados(Long estudianteId) {
        if (!estudianteRepository.existsById(estudianteId)) {
            throw new EntityNotFoundException("Estudiante no encontrado con ID: " + estudianteId);
        }
        return estudianteRepository.countIncidentesByEstudianteId(estudianteId);
    }
}