package pe.gob.minedu.sigece.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.minedu.sigece.dtos.auth.CambioPasswordRequestDto;
import pe.gob.minedu.sigece.dtos.common.PagedResponse;
import pe.gob.minedu.sigece.dtos.common.PaginationMeta;
import pe.gob.minedu.sigece.dtos.usuarios.*;
import pe.gob.minedu.sigece.enums.RolNombre;
import pe.gob.minedu.sigece.exceptions.OperacionInvalidaException;
import pe.gob.minedu.sigece.exceptions.RecursoDuplicadoException;
import pe.gob.minedu.sigece.exceptions.RecursoNoEncontradoException;
import pe.gob.minedu.sigece.mappers.UsuarioMapper;
import pe.gob.minedu.sigece.models.Rol;
import pe.gob.minedu.sigece.models.Usuario;
import pe.gob.minedu.sigece.persistence.repositories.IRolRepository;
import pe.gob.minedu.sigece.persistence.repositories.IUsuarioRepository;
import pe.gob.minedu.sigece.persistence.specs.UsuarioSpecification;
import pe.gob.minedu.sigece.usecases.IUsuarioService;
import pe.gob.minedu.sigece.usecases.auth.IPasswordEncoderPort;
import pe.gob.minedu.sigece.utils.ErrorCatalog;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final IRolRepository rolRepository;
    private final IPasswordEncoderPort passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponseDto registrarUsuario(UsuarioRegistroRequestDto dto) {
        log.info("Registrando usuario con username: {}", dto.username());

        if (usuarioRepository.existsByDni(dto.dni())) {
            throw new RecursoDuplicadoException(ErrorCatalog.USUARIO_DNI_DUPLICADO);
        }
        if (usuarioRepository.existsByCorreo(dto.correo())) {
            throw new RecursoDuplicadoException(ErrorCatalog.USUARIO_CORREO_DUPLICADO);
        }
        if (usuarioRepository.existsByUsername(dto.username())) {
            throw new RecursoDuplicadoException(ErrorCatalog.USUARIO_USERNAME_DUPLICADO);
        }

        Rol rol = rolRepository.findById(dto.roleId())
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.ROL_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(dto.password());
        Usuario nuevoUsuario = UsuarioMapper.toEntity(dto, rol, encodedPassword);

        return UsuarioMapper.toDto(usuarioRepository.save(nuevoUsuario));
    }

    @Override
    @Transactional
    public UsuarioResponseDto actualizarPerfil(Long usuarioId, UsuarioRequestDto dto) {
        log.info("Actualizando perfil de usuario ID: {}", usuarioId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        if (!usuario.getCorreo().equalsIgnoreCase(dto.correo()) && usuarioRepository.existsByCorreo(dto.correo())) {
            throw new RecursoDuplicadoException(ErrorCatalog.USUARIO_CORREO_DUPLICADO);
        }

        Rol rol = rolRepository.findById(dto.roleId())
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.ROL_NOT_FOUND));

        usuario.setNombres(dto.nombres().trim());
        usuario.setApellidos(dto.apellidos().trim());
        usuario.setCorreo(dto.correo().trim().toLowerCase());
        usuario.setTelefono(dto.telefono() != null ? dto.telefono().trim() : null);
        usuario.setRol(rol);
        usuario.setActivo(dto.activo());

        return UsuarioMapper.toDto(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void cambiarPassword(Long usuarioId, CambioPasswordRequestDto dto) {
        log.info("Cambiando password para usuario ID: {}", usuarioId);

        if (!dto.nuevoPassword().equals(dto.confirmacionPassword())) {
            throw new OperacionInvalidaException(ErrorCatalog.VALIDATION_ERROR, "Las contraseñas no coinciden");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        if (!passwordEncoder.matches(dto.passwordActual(), usuario.getPasswordHash())) {
            throw new OperacionInvalidaException(ErrorCatalog.INVALID_CREDENTIALS, "La contraseña actual es incorrecta");
        }

        usuario.setPasswordHash(passwordEncoder.encode(dto.nuevoPassword()));
        usuario.setPasswordActualizadoAt(OffsetDateTime.now());
        usuario.setDebeCambiarPassword(false);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void restablecerPassword(Long usuarioId) {
        log.info("Restableciendo password por defecto para usuario ID: {}", usuarioId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));

        String temporal = "Minedu@" + usuario.getDni();
        usuario.setPasswordHash(passwordEncoder.encode(temporal));
        usuario.setDebeCambiarPassword(true);
        usuario.setPasswordActualizadoAt(OffsetDateTime.now());
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void desactivarUsuario(Long usuarioId) {
        usuarioRepository.findById(usuarioId)
                .ifPresentOrElse(u -> {
                    u.setActivo(false);
                    usuarioRepository.save(u);
                }, () -> { throw new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND); });
    }

    @Override
    @Transactional
    public void reactivarUsuario(Long usuarioId) {
        usuarioRepository.findById(usuarioId)
                .ifPresentOrElse(u -> {
                    u.setActivo(true);
                    usuarioRepository.save(u);
                }, () -> { throw new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND); });
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDto obtenerPorId(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(UsuarioMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDto obtenerPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(UsuarioMapper::toDto)
                .orElseThrow(() -> new RecursoNoEncontradoException(ErrorCatalog.USUARIO_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UsuarioResponseDto> buscar(UsuarioFiltroRequestDto filtro) {
        int pageNumber = Optional.ofNullable(filtro).map(UsuarioFiltroRequestDto::page).orElse(0);
        int pageSize = Optional.ofNullable(filtro).map(UsuarioFiltroRequestDto::size).orElse(10);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        var page = usuarioRepository.findAll(UsuarioSpecification.conFiltro(filtro), pageable);
        var content = page.map(UsuarioMapper::toDto).getContent();
        var meta = new PaginationMeta(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());

        return new PagedResponse<>(content, meta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResumenResponseDto> listarPorRol(RolNombre rol) {
        return usuarioRepository.findAllByRolNombreAndActivoTrue(rol.name())
                .stream()
                .map(u -> UsuarioMapper.toResumenDto(u, usuarioRepository.countCasosActivosAsignados(u.getId())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public int contarCasosActivosAsignados(Long usuarioId) {
        return usuarioRepository.countCasosActivosAsignados(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RolResponseDto> listarRoles() {
        return rolRepository.findAll().stream().map(UsuarioMapper::toRolDto).toList();
    }
}