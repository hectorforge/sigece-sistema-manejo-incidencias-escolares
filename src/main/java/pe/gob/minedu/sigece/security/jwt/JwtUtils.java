package pe.gob.minedu.sigece.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.gob.minedu.sigece.security.UserPrincipal;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtils {

    @Value("${sigece.jwt.secret}")
    private String jwtSecret;

    @Value("${sigece.jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Value("${sigece.jwt.refresh-expiration-ms}")
    private long jwtRefreshExpirationMs;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generarAccessToken(UserPrincipal principal) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", principal.getId());
        claims.put("correo", principal.getCorreo());
        claims.put("rol", principal.getRolNombre());
        claims.put("nombres", principal.getNombres());
        claims.put("apellidos", principal.getApellidos());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(principal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generarRefreshToken(UserPrincipal principal) {
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .claim("userId", principal.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    public String getRolFromToken(String token) {
        return getClaims(token).get("rol", String.class);
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Firma o estructura JWT inválida: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("El token JWT ha expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("El payload JWT está vacío: {}", e.getMessage());
        } catch (JwtException e) {
            log.error("Error al procesar el token JWT: {}", e.getMessage());
        }
        return false;
    }
}
