package pe.gob.minedu.sigece.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.minedu.sigece.dtos.auth.AuthResponseDto;
import pe.gob.minedu.sigece.dtos.auth.LoginRequestDto;
import pe.gob.minedu.sigece.dtos.auth.RefreshTokenRequestDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioMantenimientoResponseDto;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.security.IUsuarioActualProvider;
import pe.gob.minedu.sigece.usecases.IAuthService;
import pe.gob.minedu.sigece.web.contracts.IAuthContractApi;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthController implements IAuthContractApi {

    private final IAuthService authService;
    private final IUsuarioActualProvider usuarioActualProvider;

    @Override
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(LoginRequestDto dto, HttpServletRequest request) {
        AuthResponseDto data = authService.autenticar(dto);
        return ResponseEntity.ok(ApiResponse.success(
                data,
                "Autenticación exitosa",
                HttpStatus.OK,
                request.getRequestURI(),
                getTraceId(request)
        ));
    }

    @Override
    public ResponseEntity<ApiResponse<AuthResponseDto>> refresh(RefreshTokenRequestDto dto, HttpServletRequest request) {
        AuthResponseDto data = authService.refrescarToken(dto);
        return ResponseEntity.ok(ApiResponse.success(
                data,
                "Token renovado exitosamente",
                HttpStatus.OK,
                request.getRequestURI(),
                getTraceId(request)
        ));
    }

    @Override
    public ResponseEntity<ApiResponse<Void>> logout(RefreshTokenRequestDto dto, HttpServletRequest request) {
        authService.cerrarSesion(dto);
        return ResponseEntity.ok(ApiResponse.success(
                null,
                "Sesión cerrada exitosamente",
                HttpStatus.OK,
                request.getRequestURI(),
                getTraceId(request)
        ));
    }

    @Override
    public ResponseEntity<ApiResponse<UsuarioMantenimientoResponseDto>> me(HttpServletRequest request) {
        Long usuarioId = usuarioActualProvider.getUsuarioActualId();
        UsuarioMantenimientoResponseDto data = authService.obtenerSesionActual(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(
                data,
                "Datos de sesión actual obtenidos exitosamente",
                HttpStatus.OK,
                request.getRequestURI(),
                getTraceId(request)
        ));
    }

    private String getTraceId(HttpServletRequest request) {
        String traceId = (String) request.getAttribute("traceId");
        return traceId != null ? traceId : UUID.randomUUID().toString();
    }
}
