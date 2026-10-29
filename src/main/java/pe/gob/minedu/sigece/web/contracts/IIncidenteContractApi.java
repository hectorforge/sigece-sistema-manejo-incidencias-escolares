package pe.gob.minedu.sigece.web.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.incidentes.*;
import pe.gob.minedu.sigece.enums.EstadoIncidente;

import java.util.List;
import java.util.UUID;

@Tag(name = "Incidentes", description = "Registro, seguimiento, resolución, cierre y derivación a SíSeVe")
@RequestMapping("/api/v1/incidentes")
public interface IIncidenteContractApi {

    // Al activar seguridad: @PreAuthorize("hasAnyRole('TUTOR','PSICOLOGO')")
    @Operation(summary = "Registrar incidente", description = "Abre el expediente. Roles: TUTOR, PSICOLOGO.")
    @PostMapping
    ResponseEntity<ApiResponse<IncidenteResponseDto>> registrarIncidente(
            @Valid @RequestBody IncidenteRequestDto dto, HttpServletRequest request);

    @Operation(summary = "Obtener expediente completo", description = "Incluye involucrados, seguimiento, evidencias y notificaciones. El TUTOR solo accede a casos de sus aulas (RN04).")
    @GetMapping("/{incidenteId}")
    ResponseEntity<ApiResponse<IncidenteExpedienteResponseDto>> obtenerExpediente(
            @PathVariable UUID incidenteId, HttpServletRequest request);

    @Operation(summary = "Búsqueda paginada y filtrada", description = "Filtros por término, estado, gravedad, tipo, fechas y reincidencia. El TUTOR ve solo sus aulas (RN04).")
    @GetMapping("/buscar")
    ResponseEntity<ApiResponse<List<IncidenteResumenResponseDto>>> buscar(
            @ModelAttribute IncidenteFiltroRequestDto filtro, HttpServletRequest request);

    @GetMapping
    ResponseEntity<ApiResponse<List<IncidenteResumenResponseDto>>> listarTodos(
            HttpServletRequest request
    );

    // @PreAuthorize("hasAnyRole('ADMINISTRADOR','PSICOLOGO')")
    @Operation(summary = "Clasificar gravedad", description = "Roles: ADMINISTRADOR, PSICOLOGO.")
    @PatchMapping("/{incidenteId}/gravedad")
    ResponseEntity<ApiResponse<IncidenteResponseDto>> clasificarGravedad(
            @PathVariable UUID incidenteId, @Valid @RequestBody ClasificarGravedadRequestDto dto, HttpServletRequest request);

    // @PreAuthorize("hasAnyRole('ADMINISTRADOR','PSICOLOGO')")
    @Operation(summary = "Asignar o reasignar responsable", description = "Roles: ADMINISTRADOR, PSICOLOGO.")
    @PatchMapping("/{incidenteId}/responsable")
    ResponseEntity<ApiResponse<IncidenteResponseDto>> asignarResponsable(
            @PathVariable UUID incidenteId, @Valid @RequestBody AsignarResponsableRequestDto dto, HttpServletRequest request);

    // @PreAuthorize("hasAnyRole('ADMINISTRADOR','PSICOLOGO')")
    @Operation(summary = "Cambiar estado", description = "No admite RESUELTO ni CERRADO: usar /resolucion y /cierre.")
    @PatchMapping("/{incidenteId}/estado")
    ResponseEntity<ApiResponse<IncidenteResponseDto>> cambiarEstado(
            @PathVariable UUID incidenteId, @Valid @RequestBody CambiarEstadoRequestDto dto, HttpServletRequest request);

    @Operation(summary = "Transiciones de estado permitidas")
    @GetMapping("/{incidenteId}/transiciones")
    ResponseEntity<ApiResponse<List<EstadoIncidente>>> transicionesPermitidas(
            @PathVariable UUID incidenteId, HttpServletRequest request);

    // @PreAuthorize("hasRole('PSICOLOGO')")
    @Operation(summary = "Resolver incidente", description = "Rol: PSICOLOGO. Exige estado EN_SEGUIMIENTO.")
    @PostMapping("/{incidenteId}/resolucion")
    ResponseEntity<ApiResponse<IncidenteResponseDto>> resolverIncidente(
            @PathVariable UUID incidenteId, @Valid @RequestBody(required = false) ResolverIncidenteRequestDto dto, HttpServletRequest request);

    @Operation(summary = "Checklist previo al cierre")
    @GetMapping("/{incidenteId}/cierre/checklist")
    ResponseEntity<ApiResponse<CierreChecklistResponseDto>> evaluarRequisitosCierre(
            @PathVariable UUID incidenteId, HttpServletRequest request);

    // @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Cerrar caso con acta", description = "Rol: ADMINISTRADOR (RN06). Exige estado RESUELTO.")
    @PostMapping("/{incidenteId}/cierre")
    ResponseEntity<ApiResponse<IncidenteResponseDto>> cerrarCaso(
            @PathVariable UUID incidenteId, @Valid @RequestBody CerrarCasoRequestDto dto, HttpServletRequest request);

    // @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Registrar derivación a SíSeVe", description = "Registro manual (CU20/CU21). Rol: ADMINISTRADOR.")
    @PostMapping("/{incidenteId}/derivacion-siseve")
    ResponseEntity<ApiResponse<IncidenteResponseDto>> registrarDerivacionSiseve(
            @PathVariable UUID incidenteId, @Valid @RequestBody DerivacionSiseveRequestDto dto, HttpServletRequest request);

    @Operation(summary = "Casos pendientes de atención", description = "Casos sin movimiento por encima del umbral de días.")
    @GetMapping("/pendientes")
    ResponseEntity<ApiResponse<List<CasoPendienteResponseDto>>> listarPendientes(
            @RequestParam(defaultValue = "3") int diasUmbral, HttpServletRequest request);

    @Operation(summary = "Evaluar reincidencia de un estudiante")
    @GetMapping("/reincidencia")
    ResponseEntity<ApiResponse<ReincidenciaResponseDto>> evaluarReincidencia(
            @Valid @ModelAttribute ReincidenciaRequestDto dto, HttpServletRequest request);

    @Operation(summary = "Historial de incidentes de un estudiante")
    @GetMapping("/estudiante/{estudianteId}/historial")
    ResponseEntity<ApiResponse<List<IncidenteResumenResponseDto>>> historialPorEstudiante(
            @PathVariable Long estudianteId, HttpServletRequest request);
}