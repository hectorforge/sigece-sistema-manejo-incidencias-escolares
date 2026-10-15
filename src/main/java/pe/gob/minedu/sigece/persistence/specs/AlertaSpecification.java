package pe.gob.minedu.sigece.persistence.specs;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import pe.gob.minedu.sigece.dtos.alertas.AlertaFiltroRequestDto;
import pe.gob.minedu.sigece.models.Alerta;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class AlertaSpecification {

    public static Specification<Alerta> conFiltro(AlertaFiltroRequestDto filtro, Long usuarioId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (usuarioId != null) {
                Predicate destinoUsuario = cb.equal(root.get("usuarioDestino").get("id"), usuarioId);
                Predicate sinDestino = cb.isNull(root.get("usuarioDestino"));
                predicates.add(cb.or(destinoUsuario, sinDestino));
            }

            if (filtro != null) {
                if (filtro.tipoAlerta() != null) {
                    predicates.add(cb.equal(root.get("tipoAlerta"), filtro.tipoAlerta()));
                }

                if (filtro.estadoAlerta() != null) {
                    predicates.add(cb.equal(root.get("estadoAlerta"), filtro.estadoAlerta()));
                }

                if (filtro.fechaDesde() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("fechaGeneracion"), filtro.fechaDesde().atStartOfDay().atOffset(ZoneOffset.UTC)));
                }

                if (filtro.fechaHasta() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("fechaGeneracion"), filtro.fechaHasta().atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC)));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}