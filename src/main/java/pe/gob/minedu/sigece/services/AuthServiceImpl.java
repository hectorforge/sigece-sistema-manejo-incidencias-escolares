package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.auth.AuthResponseDto;
import pe.gob.minedu.sigece.dtos.auth.LoginRequestDto;
import pe.gob.minedu.sigece.dtos.auth.RefreshTokenRequestDto;
import pe.gob.minedu.sigece.dtos.auth.UsuarioResponseDto;
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
//    private final IPasswordEncoderPort passwordEncoder;
//    private final ITokenProviderPort tokenProvider;

    @Override
    public AuthResponseDto autenticar(LoginRequestDto dto) {
        return null;
    }

    @Override
    public AuthResponseDto refrescarToken(RefreshTokenRequestDto dto) {
        return null;
    }

    @Override
    public void cerrarSesion(RefreshTokenRequestDto dto) {

    }

    @Override
    public UsuarioResponseDto obtenerSesionActual(Long usuarioId) {
        return null;
    }
//
//    @Override
//    @Transactional
//    public AuthResponseDto autenticar(LoginRequestDto dto) {
//        log.info("Intento de autenticación para usuario: {}", dto.username());
//
//        Usuario usuario = usuarioRepository.findByUsername(dto.username().trim())
//                .orElseThrow(() -> new OperacionInvalidaException(ErrorCatalog.INVALID_CREDENTIALS));
//
//        if (!usuario.getActivo()) {
//            throw new OperacionInvalidaException(ErrorCatalog.ACCOUNT_DISABLED);
//        }
//
//        if (usuario.getBloqueadoHasta() != null && usuario.getBloqueadoHasta().isAfter(OffsetDateTime.now())) {
//            throw new OperacionInvalidaException(ErrorCatalog.ACCOUNT_DISABLED, "Cuenta bloqueada temporalmente por intentos fallidos");
//        }
//
//        if (!passwordEncoder.matches(dto.password(), usuario.getPasswordHash())) {
//            usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
//            if (usuario.getIntentosFallidos() >= 5) {
//                usuario.setBloqueadoHasta(OffsetDateTime.now().plusMinutes(15));
//            }
//            usuarioRepository.save(usuario);
//            throw new OperacionInvalidaException(ErrorCatalog.INVALID_CREDENTIALS);
//        }
//
//        usuario.setIntentosFallidos(0);
//        usuario.setBloqueadoHasta(null);
//        usuario.setUltimoAcceso(OffsetDateTime.now());
//        usuarioRepository.save(usuario);
//
//        String access = tokenProvider.generarAccessToken(usuario);
//        String refresh = tokenProvider.generarRefreshToken(usuario);
//
//        return new AuthResponseDto(
//                access,
//                refresh,
//                "Bearer",
//                tokenProvider.obtenerTiempoExpiracionSegundos(),
//                UsuarioMapper.toDto(usuario)
//        );
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public AuthResponseDto refrescarToken(RefreshTokenRequestDto dto) {
//        if (!tokenProvider.validarToken(dto.refreshToken())) {
//            throw new OperacionInvalidaException(ErrorCatalog.UNAUTHENTICATED, "El refresh token ha expirado o es inválido");
//        }
//
//        String username = tokenProvider.extraerUsername(dto.refreshToken());
//        Usuario usuario = usuarioRepository.findByUsername(username)
//                .filter(Usuario::getActivo)
//                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));
//
//        String access = tokenProvider.generarAccessToken(usuario);
//        String refresh = tokenProvider.generarRefreshToken(usuario);
//
//        return new AuthResponseDto(
//                access,
//                refresh,
//                "Bearer",
//                tokenProvider.obtenerTiempoExpiracionSegundos(),
//                UsuarioMapper.toDto(usuario)
//        );
//    }
//
//    @Override
//    public void cerrarSesion(RefreshTokenRequestDto dto) {
//        log.info("Cerrando sesión para el token provisto");
//        // Las implementaciones de tokenProvider revocan tokens en lista negra Redis/DB si aplica
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public AuthUsuarioResponseDto obtenerSesionActual(Long usuarioId) {
//        return usuarioRepository.findById(usuarioId)
//                .map(UsuarioMapper::toAuthDto)
//                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));
//    }
}