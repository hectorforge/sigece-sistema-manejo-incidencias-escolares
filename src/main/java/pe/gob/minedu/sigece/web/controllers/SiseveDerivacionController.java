package pe.gob.minedu.sigece.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.siseve.SiseveDerivacionRequestDto;
import pe.gob.minedu.sigece.dtos.siseve.SiseveDerivacionResponseDto;
import pe.gob.minedu.sigece.dtos.siseve.SiseveEvaluacionResponseDto;
import pe.gob.minedu.sigece.dtos.siseve.SisevePendienteResponseDto;
import pe.gob.minedu.sigece.security.IUsuarioActualProvider;
import pe.gob.minedu.sigece.usecases.ISiseveDerivacionService;
import pe.gob.minedu.sigece.web.contracts.ISiseveDerivacionContractApi;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SiseveDerivacionController extends BaseController implements ISiseveDerivacionContractApi {

    private final ISiseveDerivacionService siseveService;
    private final IUsuarioActualProvider usuarioActual;

    @Override
    public ResponseEntity<ApiResponse<List<SisevePendienteResponseDto>>> listarPendientes(HttpServletRequest request) {
        List<SisevePendienteResponseDto> pendientes = siseveService.listarPendientesDeDerivacionObligatoria();
        return ok(pendientes, "Casos pendientes de derivación a SíSeVe recuperados con éxito", request);
    }

    @Override
    public ResponseEntity<ApiResponse<SiseveEvaluacionResponseDto>> evaluarDerivacion(UUID incidenteId, HttpServletRequest request) {
        SiseveEvaluacionResponseDto evaluacion = siseveService.evaluarDerivacion(incidenteId);
        return ok(evaluacion, "Evaluación de derivación completada", request);
    }

    @Override
    public ResponseEntity<ApiResponse<SiseveDerivacionResponseDto>> consultarEstado(UUID incidenteId, HttpServletRequest request) {
        SiseveDerivacionResponseDto estado = siseveService.consultarEstadoDerivacion(incidenteId);
        return ok(estado, "Estado de derivación a SíSeVe obtenido", request);
    }

    @Override
    public ResponseEntity<ApiResponse<SiseveDerivacionResponseDto>> derivarCaso(UUID incidenteId, SiseveDerivacionRequestDto dto, HttpServletRequest request) {
        Long usuarioId = usuarioActual.obtenerId(request);
        SiseveDerivacionResponseDto derivacion = siseveService.derivarCaso(incidenteId, dto, usuarioId);
        return ok(derivacion, "El caso fue derivado a la plataforma SíSeVe satisfactoriamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<List<SiseveDerivacionResponseDto>>> listarDerivados(HttpServletRequest request) {
        List<SiseveDerivacionResponseDto> derivados = siseveService.listarDerivados();
        return ok(derivados, "Casos derivados a SíSeVe recuperados con éxito", request);
    }

    private <T> ResponseEntity<ApiResponse<T>> ok(T data, String msg, HttpServletRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(data, msg, HttpStatus.OK, request.getRequestURI(), getTraceId(request))
        );
    }
}