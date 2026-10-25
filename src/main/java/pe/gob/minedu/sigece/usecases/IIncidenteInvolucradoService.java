package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.involucrados.InvolucradoRequestDto;
import pe.gob.minedu.sigece.dtos.involucrados.InvolucradoResponseDto;
import pe.gob.minedu.sigece.dtos.involucrados.InvolucradosAgrupadosResponseDto;
import pe.gob.minedu.sigece.dtos.involucrados.RemoverInvolucradoRequestDto;
import pe.gob.minedu.sigece.enums.RolInvolucrado;

import java.util.List;
import java.util.UUID;

/**
 * Servicio encargado de gestionar a los participantes (víctimas, agresores, testigos) en un incidente.
 * Casos de uso asociados: CU07, CU08, CU09, CU18.
 */
public interface IIncidenteInvolucradoService {

    /**
     * Agrega a un estudiante o miembro de la comunidad educativa como participante del expediente.
     * <p>Caso de uso: <b>CU07 · Registrar incidencia, CU08 · Gestionar involucrados</b></p>
     * <p>Rol: Personal educativo (Tutor, Psicólogo)</p>
     *
     * @param incidenteId Identificador UUID del incidente.
     * @param dto         Información del participante, su rol (víctima, agresor, testigo) y relato.
     * @param usuarioId   ID del usuario que registra al involucrado.
     * @return Involucrado asignado.
     */
    InvolucradoResponseDto agregarInvolucrado(UUID incidenteId, InvolucradoRequestDto dto, Long usuarioId);

    /**
     * Remueve a un involucrado del caso registrando la justificación requerida para la auditoría.
     * <p>Caso de uso: <b>CU08 · Gestionar involucrados</b></p>
     * <p>Rol: Personal educativo (Tutor, Psicólogo)</p>
     *
     * @param involucradoId Identificador del registro del involucrado.
     * @param dto           Motivo y sustento formal del retiro.
     * @param usuarioId     ID del usuario que efectúa el retiro.
     */
    void removerInvolucrado(UUID incidenteId, Long involucradoId, RemoverInvolucradoRequestDto dto, Long usuarioId);

    /**
     * Lista todos los participantes asociados a un incidente.
     * <p>Caso de uso: <b>CU08 · Gestionar involucrados, CU09 · Consultar incidencia</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param incidenteId Identificador UUID del incidente.
     * @return Lista de involucrados en el caso.
     */
    List<InvolucradoResponseDto> listarPorIncidente(UUID incidenteId);

    /**
     * Obtiene a los participantes agrupados por su rol (víctimas, agresores y testigos).
     * <p>Caso de uso: <b>CU08 · Gestionar involucrados, CU09 · Consultar incidencia</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param incidenteId Identificador UUID del incidente.
     * @return Estructura agrupada de involucrados.
     */
    InvolucradosAgrupadosResponseDto listarAgrupadosPorRol(UUID incidenteId);

    /**
     * Lista las apariciones de un estudiante vinculadas a un rol específico (ej. cuántas veces fue víctima o agresor).
     * <p>Caso de uso: <b>CU08 · Gestionar involucrados, CU18 · Generar alertas</b></p>
     * <p>Rol: Personal educativo, Scheduler</p>
     *
     * @param estudianteId Identificador del estudiante.
     * @param rol          Rol de involucrado a consultar.
     * @return Lista de participaciones del estudiante en dicho rol.
     */
    List<InvolucradoResponseDto> listarPorEstudianteYRol(Long estudianteId, RolInvolucrado rol);
}