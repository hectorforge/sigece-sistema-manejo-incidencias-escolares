package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.tutor.TutorAulaRequestDto;
import pe.gob.minedu.sigece.dtos.tutor.TutorAulaResponseDto;
import pe.gob.minedu.sigece.enums.Nivel;
import pe.gob.minedu.sigece.exceptions.OperacionInvalidaException;
import pe.gob.minedu.sigece.exceptions.RecursoDuplicadoException;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.mappers.TutorAulaMapper;
import pe.gob.minedu.sigece.models.TutorAula;
import pe.gob.minedu.sigece.models.Usuario;
import pe.gob.minedu.sigece.persistence.repositories.ITutorAulaRepository;
import pe.gob.minedu.sigece.persistence.repositories.IUsuarioRepository;
import pe.gob.minedu.sigece.usecases.ITutorAulaService;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.time.Year;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TutorAulaServiceImpl implements ITutorAulaService {
    private static final String ROL_TUTOR = "TUTOR";   // ajusta si tu Rol usa otro campo

    private final ITutorAulaRepository tutorAulaRepository;
    private final IUsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public TutorAulaResponseDto asignarAula(TutorAulaRequestDto dto) {
        Usuario tutor = usuarioRepository.findById(dto.usuarioId())
                .filter(Usuario::getActivo)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        // La BD no valida el rol (lo hace la aplicación, según el esquema)
        if (!ROL_TUTOR.equals(tutor.getRol().getNombre())) {
            throw new OperacionInvalidaException(ErrorCatalog.TUTOR_AULA_ROL_INVALIDO);
        }

        Nivel nivel = dto.nivel();
        String grado = dto.grado().trim();
        String seccion = dto.seccion().trim().toUpperCase();
        int anio = dto.anioEscolar() != null ? dto.anioEscolar() : Year.now().getValue();

        Optional<TutorAula> existente = tutorAulaRepository
                .findByUsuarioIdAndNivelAndGradoAndSeccionAndAnioEscolar(
                        tutor.getId(), nivel, grado, seccion, anio);

        if (existente.isPresent()) {
            TutorAula ta = existente.get();
            if (ta.isActivo()) {
                throw new RecursoDuplicadoException(ErrorCatalog.TUTOR_AULA_DUPLICADA);
            }
            ta.setActivo(true);                    // misma aula dada de baja: se reactiva
            return TutorAulaMapper.toDto(tutorAulaRepository.save(ta));
        }

        TutorAula ta = new TutorAula();
        ta.setUsuario(tutor);
        ta.setNivel(nivel);
        ta.setGrado(grado);
        ta.setSeccion(seccion);
        ta.setAnioEscolar(anio);
        ta.setActivo(true);

        log.info("Aula {} {} {} ({}) asignada al tutor ID: {}", nivel, grado, seccion, anio, tutor.getId());
        return TutorAulaMapper.toDto(tutorAulaRepository.save(ta));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TutorAulaResponseDto> listarPorTutor(Long usuarioId, Integer anioEscolar) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND);
        }
        List<TutorAula> lista = anioEscolar != null
                ? tutorAulaRepository.findByUsuarioIdAndAnioEscolarAndActivoTrue(usuarioId, anioEscolar)
                : tutorAulaRepository.findByUsuarioIdOrderByAnioEscolarDescNivelAscGradoAscSeccionAsc(usuarioId);
        return lista.stream().map(TutorAulaMapper::toDto).toList();
    }

    @Override
    public List<TutorAulaResponseDto> listarTutoresPorAula(Nivel nivel, String grado, String seccion, Integer anioEscolar) {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TutorAulaResponseDto> listarTutoresPorAula(String nivel, String grado, String seccion, Integer anioEscolar) {
        int anio = anioEscolar != null ? anioEscolar : Year.now().getValue();
        return tutorAulaRepository
                .findByNivelAndGradoAndSeccionAndAnioEscolarAndActivoTrue(
                        nivel, grado.trim(), seccion.trim().toUpperCase(), anio)
                .stream().map(TutorAulaMapper::toDto).toList();
    }

    @Override
    @Transactional
    public void desactivar(Long tutorAulaId) {
        TutorAula ta = buscar(tutorAulaId);
        ta.setActivo(false);
        tutorAulaRepository.save(ta);
    }

    @Override
    @Transactional
    public void reactivar(Long tutorAulaId) {
        TutorAula ta = buscar(tutorAulaId);
        ta.setActivo(true);
        tutorAulaRepository.save(ta);
    }

    private TutorAula buscar(Long id) {
        return tutorAulaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.TUTOR_AULA_NOT_FOUND));
    }
}
