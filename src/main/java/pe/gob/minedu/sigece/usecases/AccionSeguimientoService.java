package pe.gob.minedu.sigece.usecases;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Bitácora de trazabilidad de medidas correctivas, entrevistas y
 * derivaciones psicológicas. Evita la ruptura entre el registro
 * inicial del caso y su efectivo cierre o resolución.
 */
public interface AccionSeguimientoService {

    /**
     * Registra una nueva acción de seguimiento (entrevista, derivación
     * a psicología, medida formativa, derivación a DEMUNA/CEM, acta de
     * compromiso, etc.) sobre un expediente en curso.
     */
    AccionSeguimientoDTO registrarAccion(UUID incidenteId, AccionSeguimientoDTO dto, Long usuarioId);

    /**
     * Actualiza el estado de avance de una acción (PENDIENTE -> EN_PROCESO -> COMPLETADA).
     */
    AccionSeguimientoDTO actualizarEstadoAccion(Long accionId, EstadoAccion nuevoEstado, Long usuarioId);

    /**
     * Reprograma la fecha de próxima revisión de una acción de seguimiento
     * (p. ej. cita de reevaluación psicológica).
     */
    AccionSeguimientoDTO reprogramarProximaRevision(Long accionId, LocalDate nuevaFecha, Long usuarioId);

    /**
     * Lista cronológica de todas las acciones ejecutadas sobre un incidente,
     * insumo principal para reconstruir la línea de tiempo del caso.
     */
    List<AccionSeguimientoDTO> listarPorIncidente(UUID incidenteId);

    /**
     * Lista las acciones pendientes o en proceso asignadas a un usuario
     * (tutor/psicólogo), para su bandeja personal de tareas.
     */
    List<AccionSeguimientoDTO> listarPendientesPorUsuario(Long usuarioId);

    /**
     * Identifica acciones cuya fecha_proxima_revision ya venció sin haberse
     * completado, para alimentar alertas automáticas al Comité de Bienestar.
     */
    List<AccionSeguimientoDTO> listarAccionesVencidas();

    /**
     * Valida si un incidente cuenta con todas sus acciones en estado
     * COMPLETADA, condición previa obligatoria para permitir el cierre
     * del expediente (regla de negocio de IncidenteService.cerrarCaso).
     */
    boolean todasLasAccionesCompletadas(UUID incidenteId);

    /**
     * Filtra el historial de acciones por tipo (p. ej. todas las
     * DERIVACION_DEMUNA_CEM en un periodo), para reportes estadísticos
     * ante UGEL.
     */
    List<AccionSeguimientoDTO> listarPorTipo(TipoAccion tipo, LocalDate desde, LocalDate hasta);
}