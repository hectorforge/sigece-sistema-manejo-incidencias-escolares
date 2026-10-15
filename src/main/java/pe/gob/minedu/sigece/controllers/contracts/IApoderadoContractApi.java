package pe.gob.minedu.sigece.controllers.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoResponseDto;
import pe.gob.minedu.sigece.dtos.apoderados.VinculoFamiliarResponseDto;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;

import java.util.List;

@Tag(
        name = "Apoderados",
        description = "Endpoints para la gestión, consulta, vinculación y asignación de apoderados"
)
public interface IApoderadoContractApi {

    @Operation(
            summary = "Registrar nuevo apoderado",
            description = "Crea un registro de apoderado validando que no exista previamente el número de documento."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Apoderado registrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos o error de validación",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Conflicto: Ya existe un apoderado registrado con el documento proporcionado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping
    ResponseEntity<ApiResponse<ApoderadoResponseDto>> registrarApoderado(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del apoderado a registrar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ApoderadoRequestDto.class))
            )
            @Valid @RequestBody ApoderadoRequestDto dto
    );

    @Operation(
            summary = "Actualizar datos de contacto",
            description = "Actualiza el teléfono y correo electrónico de un apoderado registrado."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Datos de contacto actualizados exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Formato de teléfono o correo inválido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Apoderado no encontrado con el ID especificado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PatchMapping("/{apoderadoId}/contacto")
    ResponseEntity<ApiResponse<ApoderadoResponseDto>> actualizarDatosContacto(
            @Parameter(description = "ID único del apoderado", example = "1", required = true)
            @PathVariable Long apoderadoId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos de contacto del apoderado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ApoderadoRequestDto.class))
            )
            @Valid @RequestBody ApoderadoRequestDto dto
    );

    @Operation(
            summary = "Obtener apoderado por ID",
            description = "Recupera los datos del apoderado mediante su identificador primario."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Apoderado encontrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Apoderado no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/{apoderadoId}")
    ResponseEntity<ApiResponse<ApoderadoResponseDto>> obtenerPorId(
            @Parameter(description = "ID único del apoderado", example = "1", required = true)
            @PathVariable Long apoderadoId
    );

    @Operation(
            summary = "Obtener apoderado por número de documento",
            description = "Consulta y recupera los datos del apoderado a través de su número de DNI o carné de extranjería."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Apoderado encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "No se encontró apoderado con el documento indicado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/documento/{numeroDocumento}")
    ResponseEntity<ApiResponse<ApoderadoResponseDto>> obtenerPorNumeroDocumento(
            @Parameter(description = "Número de documento de identidad", example = "72345678", required = true)
            @PathVariable String numeroDocumento
    );

    @Operation(
            summary = "Listar apoderados por estudiante",
            description = "Obtiene la lista de todos los apoderados registrados formalmente para un estudiante."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Lista de apoderados obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/estudiante/{estudianteId}")
    ResponseEntity<ApiResponse<List<ApoderadoResponseDto>>> listarPorEstudiante(
            @Parameter(description = "ID único del estudiante", example = "10", required = true)
            @PathVariable Long estudianteId
    );

    @Operation(
            summary = "Listar vínculos familiares por apoderado",
            description = "Obtiene los estudiantes asignados a cargo de un apoderado (CU05)."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Vínculos familiares obtenidos exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Apoderado no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/{apoderadoId}/vinculos")
    ResponseEntity<ApiResponse<List<VinculoFamiliarResponseDto>>> listarVinculosPorApoderado(
            @Parameter(description = "ID único del apoderado", example = "1", required = true)
            @PathVariable Long apoderadoId
    );

    @Operation(
            summary = "Búsqueda paginada y filtrada de apoderados",
            description = "Consulta apoderados aplicando criterios de filtro y devolviendo los datos con metadatos de paginación."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Resultados obtenidos y paginados correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @GetMapping("/buscar")
    ResponseEntity<ApiResponse<List<ApoderadoResponseDto>>> buscar(
            @Parameter(description = "Filtro por apellido paterno o materno", example = "Perez")
            @RequestParam(required = false) String apellido,

            @Parameter(description = "Filtro por nombres", example = "Juan")
            @RequestParam(required = false) String nombre,

            @Parameter(description = "Filtro por número de documento", example = "72345678")
            @RequestParam(required = false) String numeroDocumento,

            @Parameter(description = "Número de página (iniciando en 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Cantidad de elementos por página", example = "10")
            @RequestParam(defaultValue = "10") int size
    );

    @Operation(
            summary = "Marcar como contacto principal",
            description = "Desmarca los contactos principales previos del estudiante y designa al apoderado especificado como el principal."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Apoderado asignado exitosamente como contacto principal",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Apoderado o vínculo con el estudiante no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PatchMapping("/{apoderadoId}/estudiantes/{estudianteId}/principal")
    ResponseEntity<ApiResponse<Void>> marcarContactoPrincipal(
            @Parameter(description = "ID del estudiante", example = "10", required = true)
            @PathVariable Long estudianteId,

            @Parameter(description = "ID del apoderado a marcar como principal", example = "1", required = true)
            @PathVariable Long apoderadoId
    );
}