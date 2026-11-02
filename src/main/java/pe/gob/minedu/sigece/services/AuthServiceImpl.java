package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.auth.AuthResponseDto;
import pe.gob.minedu.sigece.dtos.auth.CambioPasswordRequestDto;
import pe.gob.minedu.sigece.dtos.auth.LoginRequestDto;
import pe.gob.minedu.sigece.dtos.auth.RefreshTokenRequestDto;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioMantenimientoResponseDto;
import pe.gob.minedu.sigece.exceptions.OperacionInvalidaException;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.mappers.UsuarioMapper;
import pe.gob.minedu.sigece.models.Usuario;
import pe.gob.minedu.sigece.persistence.repositories.IUsuarioRepository;
import pe.gob.minedu.sigece.usecases.IAuthService;
import pe.gob.minedu.sigece.usecases.auth.IPasswordEncoderPort;
import pe.gob.minedu.sigece.usecases.auth.ITokenProviderPort;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final IUsuarioRepository usuarioRepository;
    private final IPasswordEncoderPort passwordEncoder;
    private final ITokenProviderPort tokenProvider;

    @Override
    @Transactional
    public AuthResponseDto autenticar(LoginRequestDto dto) {
        log.info("Iniciando autenticación para usuario: {}", dto.username());

        Usuario usuario = usuarioRepository.findByUsername(dto.username().trim())
                .or(() -> usuarioRepository.findByCorreo(dto.username().trim()))
                .orElseThrow(() -> new OperacionInvalidaException(ErrorCatalog.INVALID_CREDENTIALS));

        // Validación 1: Cuenta activa
        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new OperacionInvalidaException(ErrorCatalog.ACCOUNT_DISABLED);
        }

        // Validación 2: Bloqueo temporal
        if (usuario.getBloqueadoHasta() != null && usuario.getBloqueadoHasta().isAfter(OffsetDateTime.now())) {
            throw new OperacionInvalidaException(
                    ErrorCatalog.ACCOUNT_DISABLED,
                    "Cuenta bloqueada temporalmente por intentos fallidos. Intente más tarde."
            );
        }

        // Validación 3: Contraseña válida
        if (!passwordEncoder.matches(dto.password(), usuario.getPasswordHash())) {
            int intentos = (usuario.getIntentosFallidos() == null ? 0 : usuario.getIntentosFallidos()) + 1;
            usuario.setIntentosFallidos(intentos);

            if (intentos >= 5) {
                usuario.setBloqueadoHasta(OffsetDateTime.now().plusMinutes(15));
                log.warn("Cuenta {} bloqueada tras 5 intentos fallidos", usuario.getUsername());
            }

            usuarioRepository.save(usuario);
            throw new OperacionInvalidaException(ErrorCatalog.INVALID_CREDENTIALS);
        }

        // Restablecer contadores y actualizar último acceso
        usuario.setIntentosFallidos(0);
        usuario.setBloqueadoHasta(null);
        usuario.setUltimoAcceso(OffsetDateTime.now());
        usuarioRepository.save(usuario);

        String accessToken = tokenProvider.generarAccessToken(usuario);
        String refreshToken = tokenProvider.generarRefreshToken(usuario);

        return new AuthResponseDto(
                accessToken,
                refreshToken,
                "Bearer",
                tokenProvider.obtenerTiempoExpiracionSegundos(),
                UsuarioMapper.toDto(usuario)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDto refrescarToken(RefreshTokenRequestDto dto) {
        if (!tokenProvider.validarToken(dto.refreshToken())) {
            throw new OperacionInvalidaException(
                    ErrorCatalog.UNAUTHENTICATED,
                    "El refresh token es inválido o ha expirado"
            );
        }

        String username = tokenProvider.extraerUsername(dto.refreshToken());
        Usuario usuario = usuarioRepository.findByUsername(username)
                .filter(u -> Boolean.TRUE.equals(u.getActivo()))
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        String nuevoAccessToken = tokenProvider.generarAccessToken(usuario);
        String nuevoRefreshToken = tokenProvider.generarRefreshToken(usuario);

        return new AuthResponseDto(
                nuevoAccessToken,
                nuevoRefreshToken,
                "Bearer",
                tokenProvider.obtenerTiempoExpiracionSegundos(),
                UsuarioMapper.toDto(usuario)
        );
    }

    @Override
    public void cerrarSesion(RefreshTokenRequestDto dto) {
        log.info("Cerrando sesión para el token provisto");
        // TODO: Posteriormente se implementara una politica de lista negra para la eliminacion logica del token
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioMantenimientoResponseDto obtenerSesionActual(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(UsuarioMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));
    }

    @Override
    @Transactional
    public void cambiarPassword(Long usuarioId, CambioPasswordRequestDto dto) {
        log.info("Iniciando cambio de contraseña para el usuario ID: {}", usuarioId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .filter(u -> Boolean.TRUE.equals(u.getActivo()))
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        // 1. Validar que la nueva contraseña y su confirmación sean exactamente iguales
        if (!dto.nuevoPassword().equals(dto.confirmacionPassword())) {
            throw new OperacionInvalidaException(
                    ErrorCatalog.BAD_REQUEST,
                    "La nueva contraseña y su confirmación no coinciden"
            );
        }

        // 2. Validar que la contraseña actual coincida
        if (!passwordEncoder.matches(dto.passwordActual(), usuario.getPasswordHash())) {
            throw new OperacionInvalidaException(
                    ErrorCatalog.INVALID_CREDENTIALS,
                    "La contraseña actual proporcionada es incorrecta"
            );
        }

        // 3. Validar que la nueva contraseña no sea idéntica a la anterior
        if (passwordEncoder.matches(dto.nuevoPassword(), usuario.getPasswordHash())) {
            throw new OperacionInvalidaException(
                    ErrorCatalog.BAD_REQUEST,
                    "La nueva contraseña no puede ser idéntica a la anterior"
            );
        }

        // 4. Encriptar y persistir
        usuario.setPasswordHash(passwordEncoder.encode(dto.nuevoPassword()));
        usuario.setDebeCambiarPassword(false);
        usuarioRepository.save(usuario);

        log.info("Contraseña actualizada exitosamente para el usuario: {}", usuario.getUsername());
    }
}
