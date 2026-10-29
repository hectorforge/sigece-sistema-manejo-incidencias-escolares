package pe.gob.minedu.sigece.web.controllers;

import java.util.List;
import java.util.UUID;

import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionEstadoRequestDto;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionRevisionRequestDto;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionSeguimientoRequestDto;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionSeguimientoResponseDto;
import pe.gob.minedu.sigece.usecases.IAccionSeguimientoService;
import pe.gob.minedu.sigece.web.contracts.IAccionSeguimientoContractApi;
import pe.gob.minedu.sigece.security.IUsuarioActualProvider;
import pe.gob.minedu.sigece.web.controllers.BaseController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccionSeguimientoController extends BaseController implements IAccionSeguimientoContractApi {

    private final IAccionSeguimientoService accionSeguimientoService;
    private final IUsuarioActualProvider usuarioActual;

    @Override
    public ResponseEntity<ApiResponse<AccionSeguimientoResponseDto>> registrarAccion(UUID incidenteId, AccionSeguimientoRequestDto dto, HttpServletRequest request) {
        AccionSeguimientoResponseDto data = accionSeguimientoService.registrarAccion(incidenteId, dto, usuarioActual.obtenerId(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(data, "Acción de seguimiento registrada exitosamente", HttpStatus.CREATED, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<AccionSeguimientoResponseDto>> actualizarEstadoAccion(Long accionId, AccionEstadoRequestDto dto, HttpServletRequest request) {
        AccionSeguimientoResponseDto data = accionSeguimientoService.actualizarEstadoAccion(accionId, dto, usuarioActual.obtenerId(request));
        return ResponseEntity.ok(ApiResponse.success(data, "Estado de la acción actualizado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<AccionSeguimientoResponseDto>> reprogramarProximaRevision(Long accionId, AccionRevisionRequestDto dto, HttpServletRequest request) {
        AccionSeguimientoResponseDto data = accionSeguimientoService.reprogramarProximaRevision(accionId, dto, usuarioActual.obtenerId(request));
        return ResponseEntity.ok(ApiResponse.success(data, "Próxima revisión reprogramada exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<AccionSeguimientoResponseDto>> obtenerPorId(Long accionId, HttpServletRequest request) {
        AccionSeguimientoResponseDto data = accionSeguimientoService.obtenerPorId(accionId);
        return ResponseEntity.ok(ApiResponse.success(data, "Acción de seguimiento recuperada exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<AccionSeguimientoResponseDto>>> listarPorIncidente(UUID incidenteId, HttpServletRequest request) {
        List<AccionSeguimientoResponseDto> data = accionSeguimientoService.listarPorIncidente(incidenteId);
        return ResponseEntity.ok(ApiResponse.success(data, "Acciones de seguimiento listadas exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<AccionSeguimientoResponseDto>>> listarPendientesPorUsuario(Long usuarioId, HttpServletRequest request) {
        List<AccionSeguimientoResponseDto> data = accionSeguimientoService.listarPendientesPorUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(data, "Acciones pendientes obtenidas exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<AccionSeguimientoResponseDto>>> listarAccionesVencidas(HttpServletRequest request) {
        List<AccionSeguimientoResponseDto> data = accionSeguimientoService.listarAccionesVencidas();
        return ResponseEntity.ok(ApiResponse.success(data, "Acciones vencidas obtenidas exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<AccionSeguimientoResponseDto>>> listarPorTipo(AccionFiltroRequestDto filtro, HttpServletRequest request) {
        List<AccionSeguimientoResponseDto> data = accionSeguimientoService.listarPorTipo(filtro);
        return ResponseEntity.ok(ApiResponse.success(data, "Acciones de seguimiento filtradas exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }
}
