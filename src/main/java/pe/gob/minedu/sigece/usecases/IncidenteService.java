package pe.gob.minedu.sigece.usecases;

import pe.edu.cibertec.sigece.domain.enums.EstadoIncidente;
import pe.edu.cibertec.sigece.domain.enums.Gravedad;
import pe.edu.cibertec.sigece.service.dto.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Orquesta el ciclo de vida del expediente de convivencia escolar,
 * conforme a las fases del D.S. N.° 004-2018-MINEDU:
 * Registro -> Clasificación -> Asignación -> Investigación -> Seguimiento -> Cierre/Derivación.
 *
 * Reemplaza el cuaderno físico de incidencias, centralizando el registro
 * y garantizando trazabilidad de cada expediente.
 */
public interface IncidenteService {

    /**
     * Registra un nuevo incidente (Fase de Registro). Genera automáticamente
     * el codigoCaso correlativo (INC-YYYY-XXXXXX) y ejecuta la detección
     * automática de reincidencia sobre los estudiantes involucrados.
     */
    IncidenteDetalleDTO registrarIncidente(IncidenteRegistroDTO dto, Long usuarioReportaId);

    /**
     * Obtiene el expediente completo (datos, involucrados, acciones,
     * adjuntos y notificaciones) para su visualización unificada.
     */
    IncidenteDetalleDTO obtenerExpedienteCompleto(UUID incidenteId);

    /**
     * Clasifica o reclasifica la gravedad del caso (Fase de Clasificación),
     * dejando trazabilidad del cambio para auditoría UGEL.
     */
    IncidenteDetalleDTO clasificarGravedad(UUID incidenteId, Gravedad nuevaGravedad, Long usuarioId);

    /**
     * Asigna o reasigna el responsable actual del caso (tutor, psicólogo,
     * miembro del Comité de Gestión del Bienestar) (Fase de Asignación).
     */
    IncidenteDetalleDTO asignarResponsable(UUID incidenteId, Long responsableUsuarioId, Long usuarioQueAsignaId);

    /**
     * Transiciona el estado del expediente validando las reglas de flujo
     * (p. ej. no se puede pasar a CERRADO sin acciones de seguimiento COMPLETADAS).
     */
    IncidenteDetalleDTO cambiarEstado(UUID incidenteId, EstadoIncidente nuevoEstado, Long usuarioId, String motivo);

    /**
     * Cierra formalmente el expediente (Fase de Cierre), validando que
     * existan acciones de seguimiento resueltas y notificaciones entregadas
     * a los apoderados correspondientes.
     */
    IncidenteDetalleDTO cerrarCaso(UUID incidenteId, String actaCierre, Long usuarioId);

    /**
     * Búsqueda paginada/filtrada para el tablero de gestión del Comité
     * de Bienestar (por estado, gravedad, nivel, grado, rango de fechas,
     * responsable asignado, etc.).
     */
    List<IncidenteResumenDTO> buscarIncidentes(IncidenteFiltroDTO filtro);

    /**
     * Lista los casos vencidos o próximos a vencer según fecha_proxima_revision
     * de sus acciones de seguimiento, para prevenir el estancamiento de expedientes.
     */
    List<AlertaSeguimientoDTO> listarCasosPendientesDeAtencion(int diasUmbral);

    /**
     * Verifica si un estudiante presenta reincidencia (2+ incidentes con
     * rolInvolucrado = AGRESOR o VICTIMA en una ventana temporal configurable)
     * y marca automáticamente el flag `reincidencia` del nuevo incidente.
     */
    ReincidenciaReporteDTO evaluarReincidencia(Long estudianteId, LocalDate desde);

    /**
     * Historial cronológico de todos los incidentes en los que participó
     * un estudiante determinado (para el análisis de patrones de conducta).
     */
    List<IncidenteResumenDTO> obtenerHistorialPorEstudiante(Long estudianteId);

    /**
     * Genera el código correlativo institucional único del caso
     * (formato INC-YYYY-XXXXXX) usado para inspección y auditoría UGEL.
     */
    String generarCodigoCaso();

    /**
     * Indicadores agregados para el dashboard directivo: casos por estado,
     * por gravedad, por nivel/grado, tiempo promedio de resolución, etc.
     */
    DashboardIndicadoresDTO obtenerIndicadoresDashboard(LocalDate desde, LocalDate hasta);
}