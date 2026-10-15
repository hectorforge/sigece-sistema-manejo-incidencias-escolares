package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.apoderados.AsignarApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.VinculoFamiliarResponseDto;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudiantePerfilResponseDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteRequestDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteResponseDto;

/**
 * Servicio encargado de gestionar el padrón escolar de estudiantes (Ley N.° 29733).
 * Casos de uso asociados: CU04, CU05, CU07.
 */
public interface IEstudianteService {

    /**
     * Registra a un nuevo estudiante en el padrón escolar de la IE.
     * <p>Caso de uso: <b>CU04 · Gestionar estudiantes</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param dto Información básica y académica del estudiante.
     * @return Estudiante registrado.
     */
    EstudianteResponseDto registrarEstudiante(EstudianteRequestDto dto);

    /**
     * Actualiza los datos personales o escolares de un estudiante.
     * <p>Caso de uso: <b>CU04 · Gestionar estudiantes</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param estudianteId Identificador del estudiante a modificar.
     * @param dto          Nuevos datos del estudiante.
     * @return Estudiante actualizado.
     */
    EstudianteResponseDto actualizarDatos(Long estudianteId, EstudianteRequestDto dto);

    /**
     * Obtiene un estudiante mediante su ID único del sistema.
     * <p>Caso de uso: <b>CU04 · Gestionar estudiantes, CU07 · Registrar incidencia</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param estudianteId Identificador del estudiante.
     * @return Datos del estudiante.
     */
    EstudianteResponseDto obtenerPorId(Long estudianteId);

    /**
     * Obtiene los datos de un estudiante mediante su código del SIAGIE.
     * <p>Caso de uso: <b>CU04 · Gestionar estudiantes</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param codigoSiagie Código oficial asignado por el SIAGIE.
     * @return Datos del estudiante.
     */
    EstudianteResponseDto obtenerPorCodigoSiagie(String codigoSiagie);

    /**
     * Obtiene el perfil integral del estudiante (datos personales, apoderados y resumen de incidencias).
     * <p>Caso de uso: <b>CU04 · Gestionar estudiantes</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param estudianteId Identificador del estudiante.
     * @return Perfil consolidado del estudiante.
     */
    EstudiantePerfilResponseDto obtenerPerfil(Long estudianteId);

    /**
     * Busca y pagina estudiantes mediante filtros (grado, sección, nombre, documento, etc.).
     * <p>Caso de uso: <b>CU04 · Gestionar estudiantes, CU07 · Registrar incidencia</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param filtro Criterios de búsqueda y paginación.
     * @return Página de estudiantes coincidentes.
     */
    PagedResponse<EstudianteResponseDto> buscar(EstudianteFiltroRequestDto filtro);

    /**
     * Desactiva lógicamente el registro de un estudiante en la IE.
     * <p>Caso de uso: <b>CU04 · Gestionar estudiantes</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param estudianteId Identificador del estudiante.
     */
    void desactivarEstudiante(Long estudianteId);

    /**
     * Reactiva el registro de un estudiante dado de baja previamente.
     * <p>Caso de uso: <b>CU04 · Gestionar estudiantes</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param estudianteId Identificador del estudiante.
     */
    void reactivarEstudiante(Long estudianteId);

    /**
     * Asocia formalmente un apoderado con un estudiante especificando el parentesco.
     * <p>Caso de uso: <b>CU04 · Gestionar estudiantes, CU05 · Gestionar apoderados</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param estudianteId Identificador del estudiante.
     * @param dto          Datos de la vinculación (apoderadoId, parentesco, esPrincipal).
     * @return Vínculo familiar establecido.
     */
    VinculoFamiliarResponseDto asignarApoderado(Long estudianteId, AsignarApoderadoRequestDto dto);

    /**
     * Remueve la relación formal entre un apoderado y un estudiante.
     * <p>Caso de uso: <b>CU04 · Gestionar estudiantes, CU05 · Gestionar apoderados</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param estudianteId Identificador del estudiante.
     * @param apoderadoId  Identificador del apoderado a desvincular.
     */
    void desasignarApoderado(Long estudianteId, Long apoderadoId);

    /**
     * Cuenta el total histórico de incidentes asociados a un estudiante (como víctima, agresor o testigo).
     * <p>Caso de uso: <b>CU04 · Gestionar estudiantes</b></p>
     * <p>Rol: Personal educativo, Administrador</p>
     *
     * @param estudianteId Identificador del estudiante.
     * @return Conteo total de incidentes en los que figura involucrado.
     */
    int contarIncidentesAsociados(Long estudianteId);
}