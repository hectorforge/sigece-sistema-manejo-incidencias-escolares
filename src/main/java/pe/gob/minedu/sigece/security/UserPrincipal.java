package pe.gob.minedu.sigece.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pe.gob.minedu.sigece.enums.RolNombre;
import pe.gob.minedu.sigece.models.Usuario;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UserPrincipal implements UserDetails {

    private final Long id;
    private final String nombres;
    private final String apellidos;
    private final String username;
    private final String correo;

    @JsonIgnore
    private final String password;

    private final String rolNombre;
    private final boolean activo;
    private final Instant bloqueadoHasta;
    private final boolean debeCambiarPassword;
    private final Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal build(Usuario usuario) {
        // 1. Obtener el enum con fallback a ROLE_INVITADO para evitar NullPointerException
        RolNombre rolEnum = (usuario.getRol() != null && usuario.getRol().getNombre() != null)
                ? usuario.getRol().getNombre()
                : RolNombre.ROLE_INVITADO;

        String nombreRol = rolEnum.name();

        // 2. Garantizar el prefijo ROLE_ requerido por Spring Security
        String authorityName = nombreRol.startsWith("ROLE_") ? nombreRol : "ROLE_" + nombreRol;

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(authorityName)
        );

        return UserPrincipal.builder()
                .id(usuario.getId())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .username(usuario.getUsername())
                .correo(usuario.getCorreo())
                .password(usuario.getPasswordHash())
                .rolNombre(nombreRol)
                .activo(usuario.getActivo() != null && usuario.getActivo())
                .bloqueadoHasta(usuario.getBloqueadoHasta() != null ? usuario.getBloqueadoHasta().toInstant() : null)
                .debeCambiarPassword(usuario.getDebeCambiarPassword() != null && usuario.getDebeCambiarPassword())
                .authorities(authorities)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (bloqueadoHasta == null) {
            return true;
        }
        return Instant.now().isAfter(bloqueadoHasta);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }
}
