package pe.gob.minedu.sigece.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import pe.gob.minedu.sigece.security.UserDetailsServiceImpl;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final HandlerExceptionResolver resolver;

    public JwtAuthenticationFilter(
            JwtUtils jwtUtils,
            UserDetailsServiceImpl userDetailsService,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = extraerJwt(request);

            if (jwt != null && jwtUtils.validarToken(jwt)) {
                String username = jwtUtils.getUsernameFromToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validar si la cuenta está desactivada
                if (!userDetails.isEnabled()) {
                    throw new DisabledException("La cuenta del usuario está desactivada.");
                }

                // Validar si la cuenta está bloqueada temporalmente
                if (!userDetails.isAccountNonLocked()) {
                    throw new LockedException("La cuenta del usuario se encuentra bloqueada.");
                }

                // Si todo está correcto, registrar la autenticación
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (DisabledException | LockedException e) {
            log.warn("Error en estado de cuenta durante la autenticación: {}", e.getMessage());
            // Delega la excepción al GlobalExceptionHandler para que responda con la estructura ApiResponse uniforme
            resolver.resolveException(request, response, null, e);
        } catch (Exception e) {
            log.error("No se pudo configurar la autenticación del usuario en el contexto de seguridad", e);
            filterChain.doFilter(request, response);
        }
    }

    private String extraerJwt(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}