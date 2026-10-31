package pe.gob.minedu.sigece.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.tiposincidencia.TipoIncidenciaRequestDto;
import pe.gob.minedu.sigece.dtos.tiposincidencia.TipoIncidenciaResponseDto;
import pe.gob.minedu.sigece.dtos.tiposincidencia.TipoInicidenciaFiltroRequestDto;
import pe.gob.minedu.sigece.enums.CategoriaSiseve;
import pe.gob.minedu.sigece.usecases.ITipoIncidenciaService;
import pe.gob.minedu.sigece.web.contracts.ITipoIncidenciaContractApi;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TipoIncidenciaController implements ITipoIncidenciaContractApi {

    private final ITipoIncidenciaService tipoIncidenciaService;

    @Override
    public ResponseEntity<ApiResponse<TipoIncidenciaResponseDto>> crear(TipoIncidenciaRequestDto dto, HttpServletRequest request) {
        TipoIncidenciaResponseDto data = tipoIncidenciaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(data, "Tipo de incidencia registrado exitosamente", HttpStatus.CREATED, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<TipoIncidenciaResponseDto>> actualizar(Long id, TipoIncidenciaRequestDto dto, HttpServletRequest request) {
        TipoIncidenciaResponseDto data = tipoIncidenciaService.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success(data, "Tipo de incidencia actualizado correctamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> desactivar(Long id, HttpServletRequest request) {
        tipoIncidenciaService.desactivar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Tipo de incidencia desactivado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> reactivar(Long id, HttpServletRequest request) {
        tipoIncidenciaService.reactivar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Tipo de incidencia reactivado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<TipoIncidenciaResponseDto>>> buscar(TipoInicidenciaFiltroRequestDto filtro, HttpServletRequest request) {
        PagedResponse<TipoIncidenciaResponseDto> response = tipoIncidenciaService.buscar(filtro);
        return ResponseEntity.ok(ApiResponse.paginated(response.content(), response.meta(), "Tipos de incidencia listados exitosamente", request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<TipoIncidenciaResponseDto>> obtenerPorId(Long id, HttpServletRequest request) {
        TipoIncidenciaResponseDto data = tipoIncidenciaService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(data, "Tipo de incidencia recuperado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<TipoIncidenciaResponseDto>>> listarTodos(HttpServletRequest request) {
        List<TipoIncidenciaResponseDto> data = tipoIncidenciaService.listarTodos();
        return ResponseEntity.ok(ApiResponse.success(data, "Catálogo completo de tipos de incidencia obtenido", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<TipoIncidenciaResponseDto>>> listarActivos(HttpServletRequest request) {
        List<TipoIncidenciaResponseDto> data = tipoIncidenciaService.listarActivos();
        return ResponseEntity.ok(ApiResponse.success(data, "Tipos de incidencia activos obtenidos exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    public ResponseEntity<ApiResponse<List<TipoIncidenciaResponseDto>>> listarPorCategoriaSiseve(CategoriaSiseve categoria, HttpServletRequest request) {
        List<TipoIncidenciaResponseDto> data = tipoIncidenciaService.listarPorCategoriaSiseve(categoria);
        return ResponseEntity.ok(ApiResponse.success(data, "Tipos de incidencia por categoría SíSeVe obtenidos exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    private String getTraceId(HttpServletRequest request) {
        String traceId = (String) request.getAttribute("traceId");
        return traceId != null ? traceId : UUID.randomUUID().toString();
    }
}