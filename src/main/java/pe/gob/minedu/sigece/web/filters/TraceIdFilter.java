package pe.gob.minedu.sigece.web.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

    public static final String TRACE_ID_KEY = "traceId";
    public static final String TRACE_HEADER = "X-Trace-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // Obtener o generar el traceId
            String traceId = request.getHeader(TRACE_HEADER);
            if (traceId == null || traceId.isBlank()) {
                traceId = UUID.randomUUID().toString();
            }

            // Guardarlo en el MDC (para que salga en log.info/warn/error)
            MDC.put(TRACE_ID_KEY, traceId);

            // Guardarlo en el Request por si el controller o exception handler lo necesitan
            request.setAttribute(TRACE_ID_KEY, traceId);

            // Devolverlo siempre en los headers de la respuesta HTTP
            response.setHeader(TRACE_HEADER, traceId);

            filterChain.doFilter(request, response);
        } finally {
            // Limpiar el MDC al finalizar el hilo
            MDC.remove(TRACE_ID_KEY);
        }
    }
}