package pe.gob.minedu.sigece.web.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.siseve.SiseveDerivacionRequestDto;
import pe.gob.minedu.sigece.dtos.siseve.SiseveDerivacionResponseDto;
import pe.gob.minedu.sigece.dtos.siseve.SiseveEvaluacionResponseDto;
import pe.gob.minedu.sigece.dtos.siseve.SisevePendienteResponseDto;

import java.util.List;
import java.util.UUID;

@Tag(name = "SíSeVe - Derivaciones", description = "Gestión de elevación, evaluación y seguimiento de casos hacia el portal nacional SíSeVe (CU20/CU21)")
@RequestMapping("/api/v1/derivaciones-siseve")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public interface ISiseveDerivacionContractApi {

    @Operation(summary = "Listar casos pendientes de derivación obligatoria",
            description = "Obtiene los incidentes clasificados como GRAVE o MUY_GRAVE que aún no han sido derivados al SíSeVe.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de pendientes obtenida exitosamente")
    })
    @GetMapping("/pendientes")
    ResponseEntity<ApiResponse<List<SisevePendienteResponseDto>>> listarPendientes(
            HttpServletRequest request
    );

    @Operation(summary = "Evaluar obligación de derivación",
            description = "Determina si el caso requiere elevación obligatoria a SíSeVe según gravedad y lineamientos D.S. N.° 004-2018-MINEDU.")
    @GetMapping("/{incidenteId}/evaluacion")
    ResponseEntity<ApiResponse<SiseveEvaluacionResponseDto>> evaluarDerivacion(
            @PathVariable UUID incidenteId,
            HttpServletRequest request
    );

    @Operation(summary = "Consultar estado de derivación de un caso",
            description = "Obtiene el código SíSeVe registrado, fecha de derivación y usuario que la gestionó.")
    @GetMapping("/{incidenteId}/estado")
    ResponseEntity<ApiResponse<SiseveDerivacionResponseDto>> consultarEstado(
            @PathVariable UUID incidenteId,
            HttpServletRequest request
    );

    @Operation(summary = "Registrar derivación hacia SíSeVe",
            description = "Registra el código de caso asignado por SíSeVe, modo de derivación y observaciones. Rol: ADMINISTRADOR.")
    @PostMapping("/{incidenteId}")
    ResponseEntity<ApiResponse<SiseveDerivacionResponseDto>> derivarCaso(
            @PathVariable UUID incidenteId,
            @Valid @RequestBody SiseveDerivacionRequestDto dto,
            HttpServletRequest request
    );
    @Operation(summary = "Listar casos ya derivados a SíSeVe")
    @GetMapping("/derivados")
    ResponseEntity<ApiResponse<List<SiseveDerivacionResponseDto>>> listarDerivados(
            HttpServletRequest request
    );
}