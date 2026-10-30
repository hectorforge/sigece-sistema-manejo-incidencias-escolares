package pe.gob.minedu.sigece.web.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.gob.minedu.sigece.dtos.auth.CambioPasswordRequestDto;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.usuarios.*;
import pe.gob.minedu.sigece.enums.RolNombre;

import java.util.List;

@Tag(name = "Usuarios y Roles", description = "Endpoints para la gestión del personal de la IE (RBAC), control de cuentas y contraseñas")
@RequestMapping("/api/v1/usuarios")
@SecurityRequirement(name = "bearerAuth")
public interface IUsuarioContractApi {

    @Operation(summary = "Registrar nuevo usuario", description = "Crea un usuario institucional con rol asignado (TUTOR, PSICOLOGO, ADMINISTRADOR).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "DNI, correo o username ya registrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    ResponseEntity<ApiResponse<UsuarioMantenimientoResponseDto>> registrarUsuario(
            @Valid @RequestBody UsuarioRegistroRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "Actualizar perfil de usuario", description = "Actualiza los datos personales, de contacto y rol del usuario.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario o rol no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/{usuarioId}")
    ResponseEntity<ApiResponse<UsuarioMantenimientoResponseDto>> actualizarPerfil(
            @Parameter(description = "ID del usuario", example = "1", required = true) @PathVariable Long usuarioId,
            @Valid @RequestBody UsuarioRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "Cambiar propia contraseña", description = "Permite al usuario autenticado modificar su contraseña actual.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contraseña actualizada exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta o discrepancia en confirmación", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/cambiar-password")
    ResponseEntity<ApiResponse<Void>> cambiarPassword(
            @Valid @RequestBody CambioPasswordRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "Restablecer contraseña (Admin)", description = "Restablece la contraseña de un usuario al formato institucional predeterminado (Minedu@{DNI}).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Contraseña restablecida correctamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping("/{usuarioId}/restablecer-password")
    ResponseEntity<ApiResponse<Void>> restablecerPassword(
            @Parameter(description = "ID del usuario", example = "1", required = true) @PathVariable Long usuarioId,
            HttpServletRequest request
    );

    @Operation(summary = "Desactivar usuario", description = "Inhabilita el acceso de un usuario al sistema (baja lógica).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario desactivado", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping("/{usuarioId}/desactivar")
    ResponseEntity<ApiResponse<Void>> desactivarUsuario(
            @Parameter(description = "ID del usuario", example = "1", required = true) @PathVariable Long usuarioId,
            HttpServletRequest request
    );

    @Operation(summary = "Reactivar usuario", description = "Rehabilita el acceso y restablece los bloqueos temporales de un usuario.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario reactivado", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PatchMapping("/{usuarioId}/reactivar")
    ResponseEntity<ApiResponse<Void>> reactivarUsuario(
            @Parameter(description = "ID del usuario", example = "1", required = true) @PathVariable Long usuarioId,
            HttpServletRequest request
    );

    @Operation(summary = "Obtener usuario por ID", description = "Recupera la ficha completa de un usuario institucional.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario recuperado", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PSICOLOGO', 'TUTOR')")
    @GetMapping("/{usuarioId}")
    ResponseEntity<ApiResponse<UsuarioMantenimientoResponseDto>> obtenerPorId(
            @Parameter(description = "ID del usuario", example = "1", required = true) @PathVariable Long usuarioId,
            HttpServletRequest request
    );

    @Operation(summary = "Obtener usuario por username", description = "Recupera el usuario a través de su nombre de usuario.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuario recuperado", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PSICOLOGO', 'TUTOR')")
    @GetMapping("/username/{username}")
    ResponseEntity<ApiResponse<UsuarioMantenimientoResponseDto>> obtenerPorUsername(
            @Parameter(description = "Username del usuario", example = "admin_colegio", required = true) @PathVariable String username,
            HttpServletRequest request
    );

    @Operation(summary = "Búsqueda paginada y filtrada de usuarios", description = "Consulta paginada con filtros por texto, rol o estado.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuarios listados exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/buscar")
    ResponseEntity<ApiResponse<List<UsuarioMantenimientoResponseDto>>> buscar(
            @ModelAttribute UsuarioFiltroRequestDto filtro,
            HttpServletRequest request
    );

    @Operation(summary = "Listar usuarios por rol", description = "Obtiene los usuarios activos con un rol específico y su carga de casos asignados (CU11).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Usuarios filtrados por rol", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PSICOLOGO', 'TUTOR')")
    @GetMapping("/rol/{rol}")
    ResponseEntity<ApiResponse<List<UsuarioResumenResponseDto>>> listarPorRol(
            @Parameter(description = "Rol a filtrar", example = "ROLE_PSICOLOGO", required = true) @PathVariable RolNombre rol,
            HttpServletRequest request
    );

    @Operation(summary = "Contar casos activos asignados", description = "Devuelve el número de expedientes activos asignados a un responsable.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Conteo obtenido", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PSICOLOGO')")
    @GetMapping("/{usuarioId}/casos-activos/conteo")
    ResponseEntity<ApiResponse<Integer>> contarCasosActivosAsignados(
            @Parameter(description = "ID del usuario", example = "1", required = true) @PathVariable Long usuarioId,
            HttpServletRequest request
    );

    @Operation(summary = "Listar roles disponibles", description = "Catálogo de roles del sistema para asignación.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Catálogo de roles", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/roles")
    ResponseEntity<ApiResponse<List<RolResponseDto>>> listarRoles(
            HttpServletRequest request
    );
}