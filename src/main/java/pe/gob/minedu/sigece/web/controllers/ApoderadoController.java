package pe.gob.minedu.sigece.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoResponseDto;
import pe.gob.minedu.sigece.dtos.apoderados.VinculoFamiliarResponseDto;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.usecases.IApoderadoService;
import pe.gob.minedu.sigece.web.contracts.IApoderadoContractApi;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ApoderadoController implements IApoderadoContractApi {

    private final IApoderadoService apoderadoService;

    @Override
    public ResponseEntity<ApiResponse<ApoderadoResponseDto>> registrarApoderado(ApoderadoRequestDto dto, HttpServletRequest request) {
        ApoderadoResponseDto data = apoderadoService.registrarApoderado(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(data, "Apoderado registrado exitosamente", HttpStatus.CREATED, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<ApoderadoResponseDto>> actualizarDatosContacto(Long apoderadoId, ApoderadoRequestDto dto, HttpServletRequest request) {
        ApoderadoResponseDto data = apoderadoService.actualizarDatosContacto(apoderadoId, dto);
        return ResponseEntity.ok(ApiResponse.success(data, "Datos de contacto actualizados correctamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<ApoderadoResponseDto>> obtenerPorId(Long apoderadoId, HttpServletRequest request) {
        ApoderadoResponseDto data = apoderadoService.obtenerPorId(apoderadoId);
        return ResponseEntity.ok(ApiResponse.success(data, "Apoderado recuperado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<ApoderadoResponseDto>> obtenerPorNumeroDocumento(String numeroDocumento, HttpServletRequest request) {
        ApoderadoResponseDto data = apoderadoService.obtenerPorNumeroDocumento(numeroDocumento);
        return ResponseEntity.ok(ApiResponse.success(data, "Apoderado recuperado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<ApoderadoResponseDto>>> listarPorEstudiante(Long estudianteId, HttpServletRequest request) {
        List<ApoderadoResponseDto> data = apoderadoService.listarPorEstudiante(estudianteId);
        return ResponseEntity.ok(ApiResponse.success(data, "Apoderados vinculados obtenidos exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<VinculoFamiliarResponseDto>>> listarVinculosPorApoderado(Long apoderadoId, HttpServletRequest request) {
        List<VinculoFamiliarResponseDto> data = apoderadoService.listarVinculosPorApoderado(apoderadoId);
        return ResponseEntity.ok(ApiResponse.success(data, "Vínculos familiares obtenidos exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<ApoderadoResponseDto>>> buscar(ApoderadoFiltroRequestDto filtro, HttpServletRequest request) {
        PagedResponse<ApoderadoResponseDto> response = apoderadoService.buscar(filtro);
        return ResponseEntity.ok(ApiResponse.paginated(response.content(), response.meta(), "Apoderados listados exitosamente", request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> marcarContactoPrincipal(Long estudianteId, Long apoderadoId, HttpServletRequest request) {
        apoderadoService.marcarContactoPrincipal(estudianteId, apoderadoId);
        return ResponseEntity.ok(ApiResponse.success(null, "Apoderado asignado como contacto principal exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    private String getTraceId(HttpServletRequest request) {
        String traceId = (String) request.getAttribute("traceId");
        return traceId != null ? traceId : UUID.randomUUID().toString();
    }
}