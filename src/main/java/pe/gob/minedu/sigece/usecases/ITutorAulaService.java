package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.tutor.TutorAulaRequestDto;
import pe.gob.minedu.sigece.dtos.tutor.TutorAulaResponseDto;
import pe.gob.minedu.sigece.enums.Nivel;

import java.util.List;

public interface ITutorAulaService {
    /**
     * Asigna un aula (nivel, grado y sección) a un tutor para un año escolar.
     * <p>Caso de uso: <b>CU01/CU02 · Gestión de usuarios</b> · Respalda RN04</p>
     * <p>Rol: ADMINISTRADOR</p>
     */
    TutorAulaResponseDto asignarAula(TutorAulaRequestDto dto);

    /**
     * Lista las aulas de un tutor (todas, o solo las de un año si se indica).
     * <p>Rol: ADMINISTRADOR, o el propio TUTOR</p>
     */
    List<TutorAulaResponseDto> listarPorTutor(Long usuarioId, Integer anioEscolar);

    List<TutorAulaResponseDto> listarTutoresPorAula(Nivel nivel, String grado, String seccion, Integer anioEscolar);

    /**
     * Lista los tutores activos de un aula en un año escolar.
     * <p>Rol: ADMINISTRADOR, PSICOLOGO</p>
     */
    List<TutorAulaResponseDto> listarTutoresPorAula(String nivel, String grado, String seccion, Integer anioEscolar);

    /** Baja lógica de la asignación. <p>Rol: ADMINISTRADOR</p> */
    void desactivar(Long tutorAulaId);

    /** Reactiva una asignación dada de baja. <p>Rol: ADMINISTRADOR</p> */
    void reactivar(Long tutorAulaId);
}
