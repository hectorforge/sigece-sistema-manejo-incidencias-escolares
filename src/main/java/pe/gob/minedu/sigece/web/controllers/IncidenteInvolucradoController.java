package pe.gob.minedu.sigece.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.involucrados.InvolucradoRequestDto;
import pe.gob.minedu.sigece.dtos.involucrados.InvolucradoResponseDto;
import pe.gob.minedu.sigece.dtos.involucrados.InvolucradosAgrupadosResponseDto;
import pe.gob.minedu.sigece.dtos.involucrados.RemoverInvolucradoRequestDto;
import pe.gob.minedu.sigece.enums.RolInvolucrado;
import pe.gob.minedu.sigece.security.IUsuarioActualProvider;
import pe.gob.minedu.sigece.usecases.IIncidenteInvolucradoService;
import pe.gob.minedu.sigece.web.contracts.IIncidenteInvolucradoContractApi;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class IncidenteInvolucradoController extends BaseController implements IIncidenteInvolucradoContractApi {

    private final IIncidenteInvolucradoService involucradoService;
    private final IUsuarioActualProvider usuarioActual;

    @Override
    public ResponseEntity<ApiResponse<InvolucradoResponseDto>> agregarInvolucrado(UUID incidenteId, InvolucradoRequestDto dto, HttpServletRequest request) {
        var data = involucradoService.agregarInvolucrado(incidenteId, dto, usuarioActual.obtenerId(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(data,
                "Involucrado agregado exitosamente", HttpStatus.CREATED, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> removerInvolucrado(UUID incidenteId, Long involucradoId, RemoverInvolucradoRequestDto dto, HttpServletRequest request) {
        involucradoService.removerInvolucrado(incidenteId, involucradoId, dto, usuarioActual.obtenerId(request));
        return ok(null, "Involucrado removido exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<List<InvolucradoResponseDto>>> listarPorIncidente(UUID incidenteId, HttpServletRequest request) {
        return ok(involucradoService.listarPorIncidente(incidenteId), "Involucrados listados exitosamente", request);
    }

    @Override
    public ResponseEntity<ApiResponse<InvolucradosAgrupadosResponseDto>> listarAgrupados(UUID incidenteId, HttpServletRequest request) {
        return ok(involucradoService.listarAgrupadosPorRol(incidenteId), "Involucrados agrupados por rol", request);
    }

    @Override
    public ResponseEntity<ApiResponse<List<InvolucradoResponseDto>>> listarPorEstudianteYRol(Long estudianteId, RolInvolucrado rol, HttpServletRequest request) {
        return ok(involucradoService.listarPorEstudianteYRol(estudianteId, rol), "Participaciones listadas exitosamente", request);
    }

    private <T> ResponseEntity<ApiResponse<T>> ok(T data, String msg, HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.success(data, msg, HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }
}
