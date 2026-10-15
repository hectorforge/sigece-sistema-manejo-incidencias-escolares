package pe.gob.minedu.sigece.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.dashboard.DashboardFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.dashboard.DashboardResponseDto;
import pe.gob.minedu.sigece.usecases.IDashboardService;
import pe.gob.minedu.sigece.web.contracts.IDashboardContractApi;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DashboardController implements IDashboardContractApi {

    private final IDashboardService dashboardService;

    @Override
    public ResponseEntity<ApiResponse<DashboardResponseDto>> obtenerIndicadores(DashboardFiltroRequestDto filtro, HttpServletRequest request) {
        DashboardResponseDto data = dashboardService.obtenerIndicadores(filtro);
        return ResponseEntity.ok(ApiResponse.success(data, "Métricas del dashboard consolidadas exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    private String getTraceId(HttpServletRequest request) {
        String traceId = (String) request.getAttribute("traceId");
        return traceId != null ? traceId : UUID.randomUUID().toString();
    }
}