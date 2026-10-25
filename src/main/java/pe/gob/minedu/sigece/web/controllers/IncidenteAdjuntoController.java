package pe.gob.minedu.sigece.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.minedu.sigece.dtos.adjuntos.*;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.security.IUsuarioActualProvider;
import pe.gob.minedu.sigece.usecases.IIncidenteAdjuntoService;
import pe.gob.minedu.sigece.web.contracts.IIncidenteAdjuntoContractApi;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class IncidenteAdjuntoController extends BaseController implements IIncidenteAdjuntoContractApi {

    private final IIncidenteAdjuntoService adjuntoService;
    private final IUsuarioActualProvider usuarioActual;

    @Override
    public ResponseEntity<ApiResponse<AdjuntoResponseDto>> subirEvidencia(UUID incidenteId, AdjuntoRequestDto dto, MultipartFile archivo, HttpServletRequest request) {
        var data = adjuntoService.subirEvidencia(incidenteId, dto, archivo, usuarioActual.obtenerId(request));
        return created(data, "Evidencia adjuntada exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<List<AdjuntoResponseDto>>> subirEvidencias(UUID incidenteId, AdjuntoRequestDto dto, List<MultipartFile> archivos, HttpServletRequest request) {
        var data = adjuntoService.subirEvidencias(incidenteId, dto, archivos, usuarioActual.obtenerId(request));
        return created(data, "Evidencias adjuntadas exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<List<AdjuntoResponseDto>>> listarPorIncidente(UUID incidenteId, HttpServletRequest request) {
        return ok(adjuntoService.listarPorIncidente(incidenteId), "Evidencias listadas exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<AdjuntoUrlResponseDto>> generarUrlDescarga(Long adjuntoId, int minutos, HttpServletRequest request) {
        int expiracion = Math.min(Math.max(minutos, 1), 60);
        return ok(adjuntoService.generarUrlDescargaTemporal(adjuntoId, expiracion), "URL temporal generada", request);
    }

    @Override
    public ResponseEntity<ApiResponse<IntegridadResponseDto>> verificarIntegridad(Long adjuntoId, HttpServletRequest request) {
        return ok(adjuntoService.verificarIntegridad(adjuntoId), "Integridad verificada", request);
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> eliminarAdjunto(Long adjuntoId, EliminarAdjuntoRequestDto dto, HttpServletRequest request) {
        adjuntoService.eliminarAdjunto(adjuntoId, dto, usuarioActual.obtenerId(request));
        return ok(null, "Evidencia eliminada exitosamente", request);
    }

    private <T> ResponseEntity<ApiResponse<T>> ok(T data, String msg, HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.success(data, msg, HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    private <T> ResponseEntity<ApiResponse<T>> created(T data, String msg, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(data, msg, HttpStatus.CREATED, request.getRequestURI(), getTraceId(request)));
    }
}
