package pe.gob.minedu.sigece.web.contracts;

import java.util.List;
import java.util.UUID;

import pe.gob.minedu.sigece.dtos.seguimiento.AccionEstadoRequestDto;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionRevisionRequestDto;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionSeguimientoRequestDto;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionSeguimientoResponseDto;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(
        name = "Acciones de seguimiento",
        description = "Endpoints para registrar, consultar y gestionar las acciones de seguimiento de las incidencias escolares"
)
@RequestMapping("/api/v1/acciones-seguimiento")
public interface IAccionSeguimientoContractApi {

    @Operation(
            summary = "Registrar acción de seguimiento",
            description = "Registra una acción asociada a una incidencia escolar y actualiza el estado de la incidencia cuando corresponda."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Acción de seguimiento registrada exitosamente",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos o incidencia cerrada",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Incidencia o usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PreAuthorize("hasAnyRole('PSICOLOGO', 'ADMINISTRADOR')")
    @PostMapping("/incidente/{incidenteId}")
    ResponseEntity<ApiResponse<AccionSeguimientoResponseDto>> registrarAccion(
            @Parameter(description = "Identificador UUID de la incidencia", required = true)
            @PathVariable UUID incidenteId,
            @Valid @RequestBody AccionSeguimientoRequestDto dto,
            HttpServletRequest request
    );

    @Operation(
            summary = "Actualizar estado de una acción",
            description = "Actualiza el estado de una acción de seguimiento. Si se marca como completada, registra la fecha de finalización."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Estado de la acción actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Acción de seguimiento no encontrada",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PreAuthorize("hasAnyRole('PSICOLOGO', 'ADMINISTRADOR')")
    @PatchMapping("/{accionId}/estado")
    ResponseEntity<ApiResponse<AccionSeguimientoResponseDto>> actualizarEstadoAccion(
            @Parameter(description = "Identificador de la acción de seguimiento", example = "1", required = true)
            @PathVariable Long accionId,
            @Valid @RequestBody AccionEstadoRequestDto dto,
            HttpServletRequest request
    );

    @Operation(
            summary = "Reprogramar próxima revisión",
            description = "Actualiza la fecha de la próxima revisión de una acción de seguimiento."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Próxima revisión reprogramada exitosamente",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Acción de seguimiento no encontrada",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PreAuthorize("hasAnyRole('PSICOLOGO', 'ADMINISTRADOR')")
    @PatchMapping("/{accionId}/revision")
    ResponseEntity<ApiResponse<AccionSeguimientoResponseDto>> reprogramarProximaRevision(
            @Parameter(description = "Identificador de la acción de seguimiento", example = "1", required = true)
            @PathVariable Long accionId,
            @Valid @RequestBody AccionRevisionRequestDto dto,
            HttpServletRequest request
    );

    @Operation(
            summary = "Obtener acción de seguimiento por ID",
            description = "Recupera los datos de una acción de seguimiento mediante su identificador."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Acción de seguimiento encontrada",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Acción de seguimiento no encontrada",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PSICOLOGO', 'TUTOR')")
    @GetMapping("/{accionId}")
    ResponseEntity<ApiResponse<AccionSeguimientoResponseDto>> obtenerPorId(
            @Parameter(description = "Identificador de la acción de seguimiento", example = "1", required = true)
            @PathVariable Long accionId,
            HttpServletRequest request
    );

    @Operation(
            summary = "Listar acciones por incidencia",
            description = "Obtiene las acciones de seguimiento asociadas a una incidencia, ordenadas por fecha de acción descendente."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Acciones de seguimiento listadas exitosamente",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PSICOLOGO', 'TUTOR')")
    @GetMapping("/incidente/{incidenteId}")
    ResponseEntity<ApiResponse<List<AccionSeguimientoResponseDto>>> listarPorIncidente(
            @Parameter(description = "Identificador UUID de la incidencia", required = true)
            @PathVariable UUID incidenteId,
            HttpServletRequest request
    );

    @Operation(
            summary = "Listar acciones pendientes por usuario",
            description = "Obtiene las acciones asignadas a un usuario cuyo estado no sea COMPLETADA."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Acciones pendientes listadas exitosamente",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PSICOLOGO')")
    @GetMapping("/usuario/{usuarioId}/pendientes")
    ResponseEntity<ApiResponse<List<AccionSeguimientoResponseDto>>> listarPendientesPorUsuario(
            @Parameter(description = "Identificador del usuario", example = "1", required = true)
            @PathVariable Long usuarioId,
            HttpServletRequest request
    );

    @Operation(
            summary = "Listar acciones vencidas",
            description = "Obtiene las acciones no completadas cuya fecha de próxima revisión sea anterior a la fecha actual."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Acciones vencidas listadas exitosamente",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PSICOLOGO')")
    @GetMapping("/vencidas")
    ResponseEntity<ApiResponse<List<AccionSeguimientoResponseDto>>> listarAccionesVencidas(
            HttpServletRequest request
    );

    @Operation(
            summary = "Buscar acciones por filtros",
            description = "Consulta acciones de seguimiento aplicando los filtros definidos en la solicitud."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Búsqueda de acciones realizada exitosamente",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PSICOLOGO', 'TUTOR')")
    @GetMapping("/buscar")
    ResponseEntity<ApiResponse<List<AccionSeguimientoResponseDto>>> listarPorTipo(
            @ModelAttribute AccionFiltroRequestDto filtro,
            HttpServletRequest request
    );
}