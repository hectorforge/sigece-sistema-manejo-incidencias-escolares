package pe.gob.minedu.sigece.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("codigo", "NO_AUTORIZADO");
        body.put("mensaje", "Credenciales inválidas o token no proporcionado / expirado.");
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("path", request.getServletPath());

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
