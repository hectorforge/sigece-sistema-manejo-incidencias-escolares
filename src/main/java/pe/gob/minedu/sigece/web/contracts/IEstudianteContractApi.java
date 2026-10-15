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
import pe.gob.minedu.sigece.dtos.apoderados.AsignarApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.VinculoFamiliarResponseDto;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteFiltroRequestDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudiantePerfilResponseDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteRequestDto;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteResponseDto;

import java.util.List;

@Tag(name = "Estudiantes", description = "Endpoints para la gestión del padrón escolar protegido (Ley N.° 29733)")
@RequestMapping("/api/v1/estudiantes")
public interface IEstudianteContractApi {

    @Operation(summary = "Registrar nuevo estudiante", description = "Crea un estudiante en el padrón validando la unicidad del código SIAGIE.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Estudiante registrado exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Código SIAGIE ya registrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping
    ResponseEntity<ApiResponse<EstudianteResponseDto>> registrarEstudiante(
            @Valid @RequestBody EstudianteRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "Actualizar datos del estudiante", description = "Modifica los datos personales o escolares del estudiante.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estudiante actualizado exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PutMapping("/{estudianteId}")
    ResponseEntity<ApiResponse<EstudianteResponseDto>> actualizarDatos(
            @Parameter(description = "ID del estudiante", example = "10", required = true) @PathVariable Long estudianteId,
            @Valid @RequestBody EstudianteRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "Obtener estudiante por ID", description = "Recupera los datos del estudiante por identificador primario.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estudiante encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/{estudianteId}")
    ResponseEntity<ApiResponse<EstudianteResponseDto>> obtenerPorId(
            @Parameter(description = "ID del estudiante", example = "10", required = true) @PathVariable Long estudianteId,
            HttpServletRequest request
    );

    @Operation(summary = "Obtener estudiante por código SIAGIE", description = "Recupera un estudiante mediante su código oficial SIAGIE.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estudiante encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Código SIAGIE no registrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/siagie/{codigoSiagie}")
    ResponseEntity<ApiResponse<EstudianteResponseDto>> obtenerPorCodigoSiagie(
            @Parameter(description = "Código oficial SIAGIE", example = "1234567890", required = true) @PathVariable String codigoSiagie,
            HttpServletRequest request
    );

    @Operation(summary = "Obtener perfil integral del estudiante", description = "Consolida datos personales, apoderados y resumen de incidencias.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Perfil recuperado", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/{estudianteId}/perfil")
    ResponseEntity<ApiResponse<EstudiantePerfilResponseDto>> obtenerPerfil(
            @Parameter(description = "ID del estudiante", example = "10", required = true) @PathVariable Long estudianteId,
            HttpServletRequest request
    );

    @Operation(summary = "Búsqueda paginada y filtrada", description = "Consulta paginada de estudiantes según filtros académicos y personales.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estudiantes listados exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/buscar")
    ResponseEntity<ApiResponse<List<EstudianteResponseDto>>> buscar(
            @ModelAttribute EstudianteFiltroRequestDto filtro,
            HttpServletRequest request
    );

    @Operation(summary = "Desactivar estudiante", description = "Efectúa la baja lógica de un estudiante en la IE.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estudiante desactivado", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PatchMapping("/{estudianteId}/desactivar")
    ResponseEntity<ApiResponse<Void>> desactivarEstudiante(
            @Parameter(description = "ID del estudiante", example = "10", required = true) @PathVariable Long estudianteId,
            HttpServletRequest request
    );

    @Operation(summary = "Reactivar estudiante", description = "Reactiva el registro de un estudiante dado de baja previamente.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estudiante reactivado", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PatchMapping("/{estudianteId}/reactivar")
    ResponseEntity<ApiResponse<Void>> reactivarEstudiante(
            @Parameter(description = "ID del estudiante", example = "10", required = true) @PathVariable Long estudianteId,
            HttpServletRequest request
    );

    @Operation(summary = "Asignar apoderado", description = "Vincula formalmente a un apoderado con el estudiante indicando parentesco.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vínculo creado exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Estudiante o apoderado no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/{estudianteId}/apoderados")
    ResponseEntity<ApiResponse<VinculoFamiliarResponseDto>> asignarApoderado(
            @Parameter(description = "ID del estudiante", example = "10", required = true) @PathVariable Long estudianteId,
            @Valid @RequestBody AsignarApoderadoRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "Desasignar apoderado", description = "Remueve la relación familiar formal entre un apoderado y el estudiante.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Vínculo removido exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @DeleteMapping("/{estudianteId}/apoderados/{apoderadoId}")
    ResponseEntity<ApiResponse<Void>> desasignarApoderado(
            @Parameter(description = "ID del estudiante", example = "10", required = true) @PathVariable Long estudianteId,
            @Parameter(description = "ID del apoderado", example = "1", required = true) @PathVariable Long apoderadoId,
            HttpServletRequest request
    );

    @Operation(summary = "Contar incidentes asociados", description = "Cuenta el total de incidencias históricas en las que figura el menor.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Conteo obtenido", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/{estudianteId}/incidentes/conteo")
    ResponseEntity<ApiResponse<Integer>> contarIncidentesAsociados(
            @Parameter(description = "ID del estudiante", example = "10", required = true) @PathVariable Long estudianteId,
            HttpServletRequest request
    );
}