package pe.gob.minedu.sigece.usecases;

import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.incidentes.*;
import pe.gob.minedu.sigece.enums.EstadoIncidente;

import java.util.List;
import java.util.UUID;

/**
 * Servicio central que gestiona el ciclo de vida del expediente de violencia escolar (D.S. N.° 004-2018-MINEDU).
 * Casos de uso asociados: CU04, CU07, CU09, CU10, CU11, CU13, CU14, CU17, CU18.
 */
public interface IIncidenteService {

    /**
     * Registra un nuevo incidente de violencia escolar y apertura el expediente.
     * <p>Caso de uso: <b>CU07 · Registrar incidencia</b></p>
     * <p>Rol: Personal educativo (Tutor, Psicólogo)</p>
     *
     * @param dto               Datos iniciales del incidente ocurrido.
     * @param usuarioReportaId  ID del usuario autenticado que emite el reporte.
     * @return Resumen del incidente registrado.
     */
    IncidenteResponseDto registrarIncidente(IncidenteRequestDto dto, Long usuarioReportaId);

    /**
     * Obtiene el expediente integral de un caso con todos sus involucrados, seguimiento y evidencias.
     * <p>Caso de uso: <b>CU09 · Consultar incidencia</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param incidenteId Identificador UUID del incidente.
     * @param usuarioId   ID del usuario solicitante (para auditoría y control de acceso).
     * @return Expediente completo y trazabilidad del caso.
     */
    IncidenteExpedienteResponseDto obtenerExpedienteCompleto(UUID incidenteId, Long usuarioId);

    /**
     * Realiza búsquedas y filtrados paginados de incidentes registrados.
     * <p>Caso de uso: <b>CU09 · Consultar incidencia</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param filtro    Criterios de filtrado por fechas, estados, niveles de gravedad, etc.
     * @param usuarioId ID del usuario que consulta (aplica visibilidad de expedientes).
     * @return Página de incidentes resumidos.
     */
    PagedResponse<IncidenteResumenResponseDto> buscarIncidentes(IncidenteFiltroRequestDto filtro, Long usuarioId);

    /**
     * Clasifica oficialmente la gravedad de un caso (LEVE, MODERADO, GRAVE).
     * <p>Caso de uso: <b>CU10 · Clasificar gravedad</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @param incidenteId Identificador UUID del incidente.
     * @param dto         Nivel de gravedad y sustentación técnica.
     * @param usuarioId   ID del directivo que clasifica el caso.
     * @return Incidente con la gravedad actualizada.
     */
    IncidenteResponseDto clasificarGravedad(UUID incidenteId, ClasificarGravedadRequestDto dto, Long usuarioId);

    /**
     * Asigna a un usuario del personal (psicólogo, tutor) como responsable del seguimiento del caso.
     * <p>Caso de uso: <b>CU11 · Asignar responsable</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @param incidenteId         Identificador UUID del incidente.
     * @param dto                 Responsable asignado y consideraciones del encargo.
     * @param usuarioQueAsignaId  ID del directivo que realiza la asignación.
     * @return Incidente con el responsable actualizado.
     */
    IncidenteResponseDto asignarResponsable(UUID incidenteId, AsignarResponsableRequestDto dto, Long usuarioQueAsignaId);

    /**
     * Realiza la transición de estado dentro del flujo del expediente escolar.
     * <p>Caso de uso: <b>CU13 · Cambiar estado, CU17 · Derivar a SíSeVe</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @param incidenteId Identificador UUID del incidente.
     * @param dto         Nuevo estado y motivo del cambio.
     * @param usuarioId   ID del usuario que autoriza y efectúa la transición.
     * @return Incidente con el nuevo estado aplicado.
     */
    IncidenteResponseDto cambiarEstado(UUID incidenteId, CambiarEstadoRequestDto dto, Long usuarioId);

    /**
     * Evalúa y devuelve las transiciones de estado legalmente válidas para el estado actual del expediente.
     * <p>Caso de uso: <b>CU13 · Cambiar estado</b></p>
     * <p>Rol: Personal de gestión (Director, Comité)</p>
     *
     * @param incidenteId Identificador UUID del incidente.
     * @return Lista de estados a los que se permite transicionar.
     */
    List<EstadoIncidente> transicionesPermitidas(UUID incidenteId);

    /**
     * Valida el cumplimiento del checklist previo al cierre de un expediente (medidas, firmas, notificaciones).
     * <p>Caso de uso: <b>CU14 · Cerrar caso</b></p>
     * <p>Rol: Comité de Bienestar</p>
     *
     * @param incidenteId Identificador UUID del incidente.
     * @return Estado de verificación de cada requisito exigido para cerrar.
     */
    CierreChecklistResponseDto evaluarRequisitosCierre(UUID incidenteId);

    /**
     * Formaliza el cierre definitivo de un caso de violencia escolar.
     * <p>Caso de uso: <b>CU14 · Cerrar caso</b></p>
     * <p>Rol: Comité de Bienestar</p>
     *
     * @param incidenteId Identificador UUID del incidente.
     * @param dto         Conclusiones de cierre, informe final y acuerdos alcanzados.
     * @param usuarioId   ID del miembro del Comité que efectúa el cierre.
     * @return Incidente en estado CERRADO.
     */
    IncidenteResponseDto cerrarCaso(UUID incidenteId, CerrarCasoRequestDto dto, Long usuarioId);

    /**
     * Obtiene los casos que han superado el umbral de días sin registrar avances o atención.
     * <p>Caso de uso: <b>CU18 · Generar alertas</b></p>
     * <p>Rol: Scheduler, Personal de gestión</p>
     *
     * @param diasUmbral Número máximo de días sin movimiento permitidos.
     * @return Lista de casos en condición de estancamiento.
     */
    List<CasoPendienteResponseDto> listarCasosPendientesDeAtencion(int diasUmbral);

    /**
     * Evalúa si los involucrados presentan reincidencia en incidentes previos registrados.
     * <p>Caso de uso: <b>CU07 · Registrar incidencia, CU18 · Generar alertas</b></p>
     * <p>Rol: Personal educativo, Scheduler</p>
     *
     * @param dto Datos del estudiante y contexto a evaluar.
     * @return Resultado del análisis de reincidencia.
     */
    ReincidenciaResponseDto evaluarReincidencia(ReincidenciaRequestDto dto);

    /**
     * Lista el resumen histórico de incidentes asociados a un estudiante determinado.
     * <p>Caso de uso: <b>CU04 · Gestionar estudiantes, CU09 · Consultar incidencia</b></p>
     * <p>Rol: Personal autorizado</p>
     *
     * @param estudianteId Identificador del estudiante.
     * @return Historial resumido de casos asociados.
     */
    List<IncidenteResumenResponseDto> obtenerHistorialPorEstudiante(Long estudianteId);

    /**
     * Genera el código institucional correlativo y correlacionado para un nuevo expediente (ej. EXP-2026-0001).
     * <p>Caso de uso: <b>CU07 · Registrar incidencia</b></p>
     * <p>Rol: Personal educativo (Tutor, Psicólogo)</p>
     *
     * @return Código único correlativo generado.
     */
    String generarCodigoCaso();

    /** Resuelve el caso. Rol: PSICOLOGO. Exige estado EN_SEGUIMIENTO. */
    IncidenteResponseDto resolverIncidente(UUID incidenteId, ResolverIncidenteRequestDto dto, Long usuarioId);

    /** Registra manualmente la derivación a SíSeVe (CU20/CU21). Rol: ADMINISTRADOR. */
    IncidenteResponseDto registrarDerivacionSiseve(UUID incidenteId, DerivacionSiseveRequestDto dto, Long usuarioId);
}