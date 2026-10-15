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
import org.springframework.web.bind.annotation.*;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.tiposincidencia.TipoIncidenciaRequestDto;
import pe.gob.minedu.sigece.dtos.tiposincidencia.TipoIncidenciaResponseDto;
import pe.gob.minedu.sigece.enums.CategoriaSiseve;

import java.util.List;

@Tag(name = "Tipos de Incidencia", description = "Endpoints para el catálogo oficial de tipologías de violencia escolar (homologado con SíSeVe)")
@RequestMapping("/api/v1/tipos-incidencia")
public interface ITipoIncidenciaContractApi {

    @Operation(summary = "Crear tipo de incidencia", description = "Registra una nueva tipología de violencia en el catálogo institucional.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Tipo de incidencia creado exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El tipo de incidencia ya existe", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping
    ResponseEntity<ApiResponse<TipoIncidenciaResponseDto>> crear(
            @Valid @RequestBody TipoIncidenciaRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "Actualizar tipo de incidencia", description = "Modifica el nombre, categoría o descripción de una tipología existente.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tipo de incidencia actualizado exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tipo de incidencia no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<TipoIncidenciaResponseDto>> actualizar(
            @Parameter(description = "ID del tipo de incidencia", example = "1", required = true) @PathVariable Long id,
            @Valid @RequestBody TipoIncidenciaRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "Desactivar tipo de incidencia", description = "Inhabilita un tipo de incidencia para impedir su selección en nuevos incidentes.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tipo de incidencia desactivado exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tipo de incidencia no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PatchMapping("/{id}/desactivar")
    ResponseEntity<ApiResponse<Void>> desactivar(
            @Parameter(description = "ID del tipo de incidencia", example = "1", required = true) @PathVariable Long id,
            HttpServletRequest request
    );

    @Operation(summary = "Reactivar tipo de incidencia", description = "Habilita nuevamente un tipo de incidencia desactivado con anterioridad.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tipo de incidencia reactivado exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tipo de incidencia no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PatchMapping("/{id}/reactivar")
    ResponseEntity<ApiResponse<Void>> reactivar(
            @Parameter(description = "ID del tipo de incidencia", example = "1", required = true) @PathVariable Long id,
            HttpServletRequest request
    );

    @Operation(summary = "Obtener tipo de incidencia por ID", description = "Recupera la información de una tipología por su identificador primario.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Tipo de incidencia recuperado", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Tipo de incidencia no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<TipoIncidenciaResponseDto>> obtenerPorId(
            @Parameter(description = "ID del tipo de incidencia", example = "1", required = true) @PathVariable Long id,
            HttpServletRequest request
    );

    @Operation(summary = "Listar todos los tipos de incidencia", description = "Devuelve el catálogo completo de tipologías (activas e inactivas).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado completo obtenido", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping
    ResponseEntity<ApiResponse<List<TipoIncidenciaResponseDto>>> listarTodos(
            HttpServletRequest request
    );

    @Operation(summary = "Listar tipos de incidencia activos", description = "Devuelve únicamente las tipologías activas para el registro de nuevos casos.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado de activos obtenido", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/activos")
    ResponseEntity<ApiResponse<List<TipoIncidenciaResponseDto>>> listarActivos(
            HttpServletRequest request
    );

    @Operation(summary = "Listar por categoría SíSeVe", description = "Filtra tipologías activas según la categoría homologada del MINEDU.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Listado por categoría obtenido", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/categoria/{categoria}")
    ResponseEntity<ApiResponse<List<TipoIncidenciaResponseDto>>> listarPorCategoriaSiseve(
            @Parameter(description = "Categoría oficial SíSeVe", example = "CIBERACOSO", required = true) @PathVariable CategoriaSiseve categoria,
            HttpServletRequest request
    );
}