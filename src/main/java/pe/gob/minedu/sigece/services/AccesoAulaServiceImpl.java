package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.enums.RolNombre;
import pe.gob.minedu.sigece.models.TutorAula;
import pe.gob.minedu.sigece.models.Usuario;
import pe.gob.minedu.sigece.persistence.repositories.ITutorAulaRepository;
import pe.gob.minedu.sigece.usecases.IAccesoAulaService;

import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccesoAulaServiceImpl implements IAccesoAulaService {

    private final ITutorAulaRepository tutorAulaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TutorAula> aulasRestringidas(Usuario usuario) {
        if (usuario.getRol().getNombre() != RolNombre.ROLE_TUTOR) {
            return null;
        }
        return tutorAulaRepository.findByUsuarioIdAndAnioEscolarAndActivoTrue(
                usuario.getId(), Year.now().getValue());
    }
}
