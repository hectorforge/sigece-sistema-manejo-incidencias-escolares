package pe.gob.minedu.sigece.controllers.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoRequestDto;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoResponseDto;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;

import java.util.List;

@Tag(
        name = "Apoderados",
        description = "Endpoints para la gestión, consulta y asignación de apoderados")
public interface IApoderadoContractApi {

    @Operation(
            summary = "Registrar nuevo apoderado",
            description = "Crea un registro de apoderado validando que no exista previamente el número de documento."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Apoderado registrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApoderadoResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto: Ya existe un apoderado con el documento proporcionado",
                    content = @Content
            )
    })
    @PostMapping
    ResponseEntity<ApoderadoResponseDto> registrarApoderado(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del apoderado a registrar",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ApoderadoRequestDto.class))
            )
            @Valid @RequestBody ApoderadoRequestDto dto
    );

    @Operation(
            summary = "Actualizar datos de contacto",
            description = "Actualiza el teléfono y correo electrónico de un apoderado existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Datos de contacto actualizados exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApoderadoResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de contacto inválidos",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Apoderado no encontrado con el ID especificado",
                    content = @Content
            )
    })
    @PatchMapping("/{apoderadoId}/contacto")
    ResponseEntity<ApoderadoResponseDto> actualizarDatosContacto(
            @Parameter(description = "ID único del apoderado", example = "1", required = true)
            @PathVariable Long apoderadoId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos de contacto (teléfono y correo)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ApoderadoRequestDto.class))
            )
            @Valid @RequestBody ApoderadoRequestDto dto
    );

    @Operation(
            summary = "Obtener apoderado por ID",
            description = "Recupera la información detallada de un apoderado mediante su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Apoderado encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApoderadoResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Apoderado no encontrado",
                    content = @Content
            )
    })
    @GetMapping("/{apoderadoId}")
    ResponseEntity<ApoderadoResponseDto> obtenerPorId(
            @Parameter(description = "ID único del apoderado", example = "1", required = true)
            @PathVariable Long apoderadoId
    );

    @Operation(
            summary = "Obtener apoderado por número de documento",
            description = "Recupera la información del apoderado consultando por su DNI, CE u otro documento."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Apoderado encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApoderadoResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró apoderado con el documento indicado",
                    content = @Content
            )
    })
    @GetMapping("/documento/{numeroDocumento}")
    ResponseEntity<ApoderadoResponseDto> obtenerPorNumeroDocumento(
            @Parameter(description = "Número de documento de identidad", example = "72345678", required = true)
            @PathVariable String numeroDocumento
    );

    @Operation(
            summary = "Listar apoderados por estudiante",
            description = "Devuelve la lista de apoderados vinculados a un estudiante determinado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista recuperada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ApoderadoResponseDto.class))
                    )
            )
    })
    @GetMapping("/estudiante/{estudianteId}")
    ResponseEntity<List<ApoderadoResponseDto>> listarPorEstudiante(
            @Parameter(description = "ID único del estudiante", example = "10", required = true)
            @PathVariable Long estudianteId
    );

    @Operation(
            summary = "Búsqueda paginada de apoderados",
            description = "Filtra y pagina apoderados por apellido, nombre y/o número de documento."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resultados paginados obtenidos correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedResponse.class))
            )
    })
    @GetMapping("/buscar")
    ResponseEntity<PagedResponse<ApoderadoResponseDto>> buscar(
            @Parameter(description = "Filtro por apellido paterno/materno", example = "Perez")
            @RequestParam(required = false) String apellido,

            @Parameter(description = "Filtro por nombres", example = "Juan")
            @RequestParam(required = false) String nombre,

            @Parameter(description = "Filtro por número de documento", example = "72345678")
            @RequestParam(required = false) String numeroDocumento
    );

    @Operation(
            summary = "Marcar como contacto principal",
            description = "Desmarca los contactos principales previos del estudiante y asigna al apoderado indicado como el principal."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Contacto principal actualizado correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró el apoderado o la relación con el estudiante",
                    content = @Content
            )
    })
    @PatchMapping("/{apoderadoId}/estudiantes/{estudianteId}/principal")
    ResponseEntity<Void> marcarContactoPrincipal(
            @Parameter(description = "ID del estudiante", example = "10", required = true)
            @PathVariable Long estudianteId,

            @Parameter(description = "ID del apoderado a marcar como principal", example = "1", required = true)
            @PathVariable Long apoderadoId
    );
}