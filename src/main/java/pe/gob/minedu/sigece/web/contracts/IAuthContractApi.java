package pe.gob.minedu.sigece.web.contracts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.gob.minedu.sigece.dtos.auth.AuthResponseDto;
import pe.gob.minedu.sigece.dtos.auth.LoginRequestDto;
import pe.gob.minedu.sigece.dtos.auth.RefreshTokenRequestDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioMantenimientoResponseDto;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;

@Tag(name = "Autenticación", description = "Endpoints para el inicio de sesión, renovación de tokens JWT y cierre de sesión")
@RequestMapping("/api/v1/auth")
public interface IAuthContractApi {

    @Operation(summary = "Iniciar sesión", description = "Valida las credenciales institucionales y emite el par de tokens JWT (Access y Refresh).")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sesión iniciada exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Credenciales mal formadas", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Credenciales inválidas o cuenta deshabilitada/bloqueada", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    ResponseEntity<ApiResponse<AuthResponseDto>> login(
            @Valid @RequestBody LoginRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "Refrescar token de acceso", description = "Emite un nuevo Access Token a partir de un Refresh Token vigente.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Token renovado exitosamente", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Refresh token expirado o inválido", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/refresh")
    ResponseEntity<ApiResponse<AuthResponseDto>> refresh(
            @Valid @RequestBody RefreshTokenRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "Cerrar sesión", description = "Invalida la sesión activa y el Refresh Token.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Sesión cerrada correctamente", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody RefreshTokenRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "Obtener perfil de sesión actual", description = "Recupera la información del usuario autenticado que realiza la solicitud.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Datos de sesión recuperados", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    ResponseEntity<ApiResponse<UsuarioMantenimientoResponseDto>> me(
            HttpServletRequest request
    );
}