package pe.gob.minedu.sigece.web.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.minedu.sigece.dtos.auth.CambioPasswordRequestDto;
import pe.gob.minedu.sigece.dtos.common.ApiResponse;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.usuarios.*;
import pe.gob.minedu.sigece.enums.RolNombre;
import pe.gob.minedu.sigece.security.IUsuarioActualProvider;
import pe.gob.minedu.sigece.usecases.IUsuarioService;
import pe.gob.minedu.sigece.web.contracts.IUsuarioContractApi;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UsuarioController implements IUsuarioContractApi {

    private final IUsuarioService usuarioService;
    private final IUsuarioActualProvider usuarioActualProvider;

    @Override
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<UsuarioMantenimientoResponseDto>> registrarUsuario(UsuarioRegistroRequestDto dto, HttpServletRequest request) {
        UsuarioMantenimientoResponseDto data = usuarioService.registrarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(data, "Usuario registrado exitosamente en el sistema", HttpStatus.CREATED, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<UsuarioMantenimientoResponseDto>> actualizarPerfil(Long usuarioId, UsuarioRequestDto dto, HttpServletRequest request) {
        UsuarioMantenimientoResponseDto data = usuarioService.actualizarPerfil(usuarioId, dto);
        return ResponseEntity.ok(ApiResponse.success(data, "Perfil de usuario actualizado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> cambiarPassword(CambioPasswordRequestDto dto, HttpServletRequest request) {
        // Se extrae el ID del usuario en sesión desde el token JWT para garantizar que no cambie la de otro usuario
        Long usuarioIdActual = usuarioActualProvider.getUsuarioActualId();
        usuarioService.cambiarPassword(usuarioIdActual, dto);
        return ResponseEntity.ok(ApiResponse.success(null, "Contraseña modificada exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> restablecerPassword(Long usuarioId, HttpServletRequest request) {
        usuarioService.restablecerPassword(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(null, "Contraseña restablecida exitosamente por el administrador", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> desactivarUsuario(Long usuarioId, HttpServletRequest request) {
        usuarioService.desactivarUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(null, "Usuario desactivado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<Void>> reactivarUsuario(Long usuarioId, HttpServletRequest request) {
        usuarioService.reactivarUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(null, "Usuario reactivado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<UsuarioMantenimientoResponseDto>> obtenerPorId(Long usuarioId, HttpServletRequest request) {
        UsuarioMantenimientoResponseDto data = usuarioService.obtenerPorId(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(data, "Usuario recuperado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<UsuarioMantenimientoResponseDto>> obtenerPorUsername(String username, HttpServletRequest request) {
        UsuarioMantenimientoResponseDto data = usuarioService.obtenerPorUsername(username);
        return ResponseEntity.ok(ApiResponse.success(data, "Usuario recuperado exitosamente por username", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<List<UsuarioMantenimientoResponseDto>>> buscar(UsuarioFiltroRequestDto filtro, HttpServletRequest request) {
        PagedResponse<UsuarioMantenimientoResponseDto> response = usuarioService.buscar(filtro);
        return ResponseEntity.ok(ApiResponse.paginated(response.content(), response.meta(), "Usuarios listados exitosamente", request.getRequestURI(), getTraceId(request)));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TUTOR')")
    public ResponseEntity<ApiResponse<List<UsuarioResumenResponseDto>>> listarPorRol(RolNombre rol, HttpServletRequest request) {
        List<UsuarioResumenResponseDto> data = usuarioService.listarPorRol(rol);
        return ResponseEntity.ok(ApiResponse.success(data, "Usuarios con rol " + rol.name() + " recuperados exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TUTOR', 'PSICOLOGO')")
    public ResponseEntity<ApiResponse<Integer>> contarCasosActivosAsignados(Long usuarioId, HttpServletRequest request) {
        int conteo = usuarioService.contarCasosActivosAsignados(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(conteo, "Conteo de casos activos asignados recuperado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<List<RolResponseDto>>> listarRoles(HttpServletRequest request) {
        List<RolResponseDto> data = usuarioService.listarRoles();
        return ResponseEntity.ok(ApiResponse.success(data, "Catálogo de roles recuperado exitosamente", HttpStatus.OK, request.getRequestURI(), getTraceId(request)));
    }

    private String getTraceId(HttpServletRequest request) {
        String traceId = (String) request.getAttribute("traceId");
        return traceId != null ? traceId : UUID.randomUUID().toString();
    }
}
