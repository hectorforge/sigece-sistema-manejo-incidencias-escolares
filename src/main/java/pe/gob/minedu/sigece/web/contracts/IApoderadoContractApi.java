package pe.gob.minedu.sigece.web.contracts;

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
import org.springframework.web.bind.annotation.*;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoResponseDto;
import pe.gob.minedu.sigece.dtos.apoderados.VinculoFamiliarResponseDto;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;

import java.util.List;

@Tag(name = "Apoderados", description = "Endpoints para la gestión, consulta, vinculación y asignación de apoderados")
@RequestMapping("/api/v1/apoderados")
public interface IApoderadoContractApi {

    @Operation(summary = "Registrar nuevo apoderado", description = "Crea un registro de apoderado validando que no exista previamente el documento.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Apoderado registrado exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Documento ya registrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    ResponseEntity<ApiResponse<ApoderadoResponseDto>> registrarApoderado(
            @Valid @RequestBody ApoderadoRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "Actualizar datos de contacto", description = "Actualiza el teléfono y correo electrónico de un apoderado registrado.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contacto actualizado exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Apoderado no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping("/{apoderadoId}/contacto")
    ResponseEntity<ApiResponse<ApoderadoResponseDto>> actualizarDatosContacto(
            @Parameter(description = "ID único del apoderado", example = "1", required = true) @PathVariable Long apoderadoId,
            @Valid @RequestBody ApoderadoRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "Obtener apoderado por ID", description = "Recupera los datos del apoderado por identificador primario.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Apoderado encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Apoderado no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TUTOR', 'PSICOLOGO')")
    @GetMapping("/{apoderadoId}")
    ResponseEntity<ApiResponse<ApoderadoResponseDto>> obtenerPorId(
            @Parameter(description = "ID único del apoderado", example = "1", required = true) @PathVariable Long apoderadoId,
            HttpServletRequest request
    );

    @Operation(summary = "Obtener apoderado por documento", description = "Consulta al apoderado mediante su DNI o carné de extranjería.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Apoderado encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Documento no registrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TUTOR', 'PSICOLOGO')")
    @GetMapping("/documento/{numeroDocumento}")
    ResponseEntity<ApiResponse<ApoderadoResponseDto>> obtenerPorNumeroDocumento(
            @Parameter(description = "Número de documento", example = "72345678", required = true) @PathVariable String numeroDocumento,
            HttpServletRequest request
    );

    @Operation(summary = "Listar apoderados por estudiante", description = "Obtiene los apoderados vinculados a un estudiante.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado obtenido exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TUTOR', 'PSICOLOGO')")
    @GetMapping("/estudiante/{estudianteId}")
    ResponseEntity<ApiResponse<List<ApoderadoResponseDto>>> listarPorEstudiante(
            @Parameter(description = "ID del estudiante", example = "10", required = true) @PathVariable Long estudianteId,
            HttpServletRequest request
    );

    @Operation(summary = "Listar vínculos por apoderado", description = "Obtiene los estudiantes asignados a un apoderado.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vínculos obtenidos", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Apoderado no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TUTOR', 'PSICOLOGO')")
    @GetMapping("/{apoderadoId}/vinculos")
    ResponseEntity<ApiResponse<List<VinculoFamiliarResponseDto>>> listarVinculosPorApoderado(
            @Parameter(description = "ID del apoderado", example = "1", required = true) @PathVariable Long apoderadoId,
            HttpServletRequest request
    );

    @Operation(summary = "Búsqueda paginada y filtrada", description = "Consulta paginada aplicando filtros dinámicos.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Búsqueda paginada exitosa", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TUTOR', 'PSICOLOGO')")
    @GetMapping("/buscar")
    ResponseEntity<ApiResponse<List<ApoderadoResponseDto>>> buscar(
            @ModelAttribute ApoderadoFiltroRequestDto filtro,
            HttpServletRequest request
    );

    @Operation(summary = "Marcar como contacto principal", description = "Establece a este apoderado como el contacto principal para el estudiante.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contacto principal actualizado", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Vínculo o entidad no encontrada", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping("/{apoderadoId}/estudiantes/{estudianteId}/principal")
    ResponseEntity<ApiResponse<Void>> marcarContactoPrincipal(
            @Parameter(description = "ID del estudiante", example = "10", required = true) @PathVariable Long estudianteId,
            @Parameter(description = "ID del apoderado", example = "1", required = true) @PathVariable Long apoderadoId,
            HttpServletRequest request
    );
}