package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.seguimiento.AccionEstadoRequestDto;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionRevisionRequestDto;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionSeguimientoRequestDto;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionSeguimientoResponseDto;

import java.util.List;
import java.util.UUID;

/**
 * Servicio encargado de la bitácora de seguimiento, medidas correctivas, compromisos y citas.
 * Casos de uso asociados: CU09, CU12, CU14, CU18, CU20.
 */
public interface IAccionSeguimientoService {

    /**
     * Registra una nueva acción o compromiso en la bitácora de seguimiento del incidente.
     * <p>Caso de uso: <b>CU12 · Gestionar seguimiento</b></p>
     * <p>Rol: Tutor, Psicólogo, Comité</p>
     *
     * @param incidenteId Identificador UUID del incidente.
     * @param dto         Detalle de la acción, tipo de medida, fecha programada y acuerdos.
     * @param usuarioId   ID del usuario que registra la acción.
     * @return Acción de seguimiento creada.
     */
    AccionSeguimientoResponseDto registrarAccion(UUID incidenteId, AccionSeguimientoRequestDto dto, Long usuarioId);

    /**
     * Actualiza el estado de cumplimiento (ej. COMPLETADA, CANCELADA) de una acción.
     * <p>Caso de uso: <b>CU12 · Gestionar seguimiento</b></p>
     * <p>Rol: Tutor, Psicólogo, Comité</p>
     *
     * @param accionId  Identificador de la acción de seguimiento.
     * @param dto       Nuevo estado y comentarios de ejecución.
     * @param usuarioId ID del usuario que actualiza el estado.
     * @return Acción con el estado modificado.
     */
    AccionSeguimientoResponseDto actualizarEstadoAccion(Long accionId, AccionEstadoRequestDto dto, Long usuarioId);

    /**
     * Reprograma la fecha proyectada de revisión o seguimiento de una acción.
     * <p>Caso de uso: <b>CU12 · Gestionar seguimiento</b></p>
     * <p>Rol: Tutor, Psicólogo, Comité</p>
     *
     * @param accionId  Identificador de la acción a reprogramar.
     * @param dto       Nueva fecha estipulada y justificación de la postergación.
     * @param usuarioId ID del usuario que reprograma.
     * @return Acción actualizada con la nueva fecha.
     */
    AccionSeguimientoResponseDto reprogramarProximaRevision(Long accionId, AccionRevisionRequestDto dto, Long usuarioId);

    /**
     * Obtiene una acción de seguimiento por su ID único.
     * <p>Caso de uso: <b>CU12 · Gestionar seguimiento</b></p>
     * <p>Rol: Tutor, Psicólogo, Comité</p>
     *
     * @param accionId Identificador de la acción.
     * @return Detalle de la acción solicitada.
     */
    AccionSeguimientoResponseDto obtenerPorId(Long accionId);

    /**
     * Lista todas las acciones de seguimiento ejecutadas y programadas para un caso.
     * <p>Caso de uso: <b>CU12 · Gestionar seguimiento, CU09 · Consultar incidencia</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param incidenteId Identificador UUID del incidente.
     * @return Lista cronológica de acciones de seguimiento.
     */
    List<AccionSeguimientoResponseDto> listarPorIncidente(UUID incidenteId);

    /**
     * Lista las acciones de seguimiento pendientes de ejecución asignadas a un usuario específico.
     * <p>Caso de uso: <b>CU12 · Gestionar seguimiento</b></p>
     * <p>Rol: Tutor, Psicólogo, Comité</p>
     *
     * @param usuarioId Identificador del usuario responsable.
     * @return Lista de acciones pendientes del usuario.
     */
    List<AccionSeguimientoResponseDto> listarPendientesPorUsuario(Long usuarioId);

    /**
     * Lista todas las acciones cuya fecha límite ha expirado y continúan pendientes en el sistema.
     * <p>Caso de uso: <b>CU18 · Generar alertas</b></p>
     * <p>Rol: Scheduler, Personal de gestión</p>
     *
     * @return Lista de acciones vencidas.
     */
    List<AccionSeguimientoResponseDto> listarAccionesVencidas();

    /**
     * Verifica si la totalidad de las acciones pactadas para un expediente se encuentran completadas.
     * <p>Caso de uso: <b>CU14 · Cerrar caso</b></p>
     * <p>Rol: Comité de Bienestar</p>
     *
     * @param incidenteId Identificador UUID del incidente.
     * @return {@code true} si todas las acciones están completadas; {@code false} de lo contrario.
     */
    boolean todasLasAccionesCompletadas(UUID incidenteId);

    /**
     * Lista acciones de seguimiento filtradas por tipo de medida o estado de avance.
     * <p>Caso de uso: <b>CU12 · Gestionar seguimiento, CU20 · Generar y exportar reportes</b></p>
     * <p>Rol: Tutor, Psicólogo, Comité, Personal de gestión</p>
     *
     * @param filtro Criterios de filtrado (tipo de acción, rango de fechas, etc.).
     * @return Lista de acciones que cumplen el filtro.
     */
    List<AccionSeguimientoResponseDto> listarPorTipo(AccionFiltroRequestDto filtro);
}