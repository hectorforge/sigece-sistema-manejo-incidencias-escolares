package pe.gob.minedu.sigece.web.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.dashboard.DashboardFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.dashboard.DashboardResponseDto;

@Tag(name = "Dashboard", description = "Endpoints de analítica y métricas de convivencia escolar (CU19)")
@RequestMapping("/api/v1/dashboard")
public interface IDashboardContractApi {

    @Operation(summary = "Obtener indicadores analíticos", description = "Calcula indicadores clave de convivencia escolar (tasas de resolución, reincidencia y distribución).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Indicadores calculados exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Filtros inválidos", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/indicadores")
    ResponseEntity<ApiResponse<DashboardResponseDto>> obtenerIndicadores(
            @ModelAttribute DashboardFiltroRequestDto filtro,
            HttpServletRequest request
    );
}