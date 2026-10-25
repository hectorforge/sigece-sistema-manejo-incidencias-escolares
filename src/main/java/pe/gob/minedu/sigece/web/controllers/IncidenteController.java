package pe.gob.minedu.sigece.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.incidentes.*;
import pe.gob.minedu.sigece.enums.EstadoIncidente;
import pe.gob.minedu.sigece.security.IUsuarioActualProvider;
import pe.gob.minedu.sigece.usecases.IIncidenteService;
import pe.gob.minedu.sigece.web.contracts.IIncidenteContractApi;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class IncidenteController extends BaseController implements IIncidenteContractApi {

    private final IIncidenteService incidenteService;
    private final IUsuarioActualProvider usuarioActual;

    @Override
    public ResponseEntity<ApiResponse<IncidenteResponseDto>> registrarIncidente(IncidenteRequestDto dto, HttpServletRequest request) {
        var data = incidenteService.registrarIncidente(dto, usuarioActual.obtenerId(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(data,
                "Incidente registrado exitosamente", HttpStatus.CREATED, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<IncidenteExpedienteResponseDto>> obtenerExpediente(UUID incidenteId, HttpServletRequest request) {
        var data = incidenteService.obtenerExpedienteCompleto(incidenteId, usuarioActual.obtenerId(request));
        return ok(data, "Expediente recuperado exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<List<IncidenteResumenResponseDto>>> buscar(IncidenteFiltroRequestDto filtro, HttpServletRequest request) {
        PagedResponse<IncidenteResumenResponseDto> r = incidenteService.buscarIncidentes(filtro, usuarioActual.obtenerId(request));
        return ResponseEntity.ok(ApiResponse.paginated(r.content(), r.meta(), "Incidentes listados exitosamente",
                request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<IncidenteResponseDto>> clasificarGravedad(UUID incidenteId, ClasificarGravedadRequestDto dto, HttpServletRequest request) {
        return ok(incidenteService.clasificarGravedad(incidenteId, dto, usuarioActual.obtenerId(request)),
                "Gravedad clasificada exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<IncidenteResponseDto>> asignarResponsable(UUID incidenteId, AsignarResponsableRequestDto dto, HttpServletRequest request) {
        return ok(incidenteService.asignarResponsable(incidenteId, dto, usuarioActual.obtenerId(request)),
                "Responsable asignado exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<IncidenteResponseDto>> cambiarEstado(UUID incidenteId, CambiarEstadoRequestDto dto, HttpServletRequest request) {
        return ok(incidenteService.cambiarEstado(incidenteId, dto, usuarioActual.obtenerId(request)),
                "Estado actualizado exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<List<EstadoIncidente>>> transicionesPermitidas(UUID incidenteId, HttpServletRequest request) {
        return ok(incidenteService.transicionesPermitidas(incidenteId), "Transiciones permitidas obtenidas", request);
    }

    @Override
    public ResponseEntity<ApiResponse<IncidenteResponseDto>> resolverIncidente(UUID incidenteId, ResolverIncidenteRequestDto dto, HttpServletRequest request) {
        return ok(incidenteService.resolverIncidente(incidenteId, dto, usuarioActual.obtenerId(request)),
                "Incidente resuelto exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<CierreChecklistResponseDto>> evaluarRequisitosCierre(UUID incidenteId, HttpServletRequest request) {
        return ok(incidenteService.evaluarRequisitosCierre(incidenteId), "Checklist de cierre evaluado", request);
    }

    @Override
    public ResponseEntity<ApiResponse<IncidenteResponseDto>> cerrarCaso(UUID incidenteId, CerrarCasoRequestDto dto, HttpServletRequest request) {
        return ok(incidenteService.cerrarCaso(incidenteId, dto, usuarioActual.obtenerId(request)),
                "Caso cerrado exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<IncidenteResponseDto>> registrarDerivacionSiseve(UUID incidenteId, DerivacionSiseveRequestDto dto, HttpServletRequest request) {
        return ok(incidenteService.registrarDerivacionSiseve(incidenteId, dto, usuarioActual.obtenerId(request)),
                "Derivación a SíSeVe registrada exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<List<CasoPendienteResponseDto>>> listarPendientes(int diasUmbral, HttpServletRequest request) {
        return ok(incidenteService.listarCasosPendientesDeAtencion(diasUmbral), "Casos pendientes listados", request);
    }

    @Override
    public ResponseEntity<ApiResponse<ReincidenciaResponseDto>> evaluarReincidencia(ReincidenciaRequestDto dto, HttpServletRequest request) {
        return ok(incidenteService.evaluarReincidencia(dto), "Reincidencia evaluada", request);
    }

    @Override
    public ResponseEntity<ApiResponse<List<IncidenteResumenResponseDto>>> historialPorEstudiante(Long estudianteId, HttpServletRequest request) {
        return ok(incidenteService.obtenerHistorialPorEstudiante(estudianteId), "Historial recuperado exitosamente", request);
    }

    private <T> ResponseEntity<ApiResponse<T>> ok(T data, String msg, HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.success(data, msg, HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }
}