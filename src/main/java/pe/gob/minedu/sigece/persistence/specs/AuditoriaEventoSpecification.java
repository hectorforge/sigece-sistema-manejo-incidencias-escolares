package pe.gob.minedu.sigece.persistence.specs;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import pe.gob.minedu.sigece.dtos.auditoria.AuditoriaFiltroRequestDto;
import pe.gob.minedu.sigece.models.AuditoriaEvento;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaEventoSpecification {

    public static Specification<AuditoriaEvento> conFiltro(AuditoriaFiltroRequestDto filtro) {
        return (root, query, cb) -> {
            if (filtro == null) return cb.conjunction();
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.accion() != null) {
                predicates.add(cb.equal(root.get("accion"), filtro.accion()));
            }

            if (filtro.entidad() != null) {
                predicates.add(cb.equal(root.get("entidad"), filtro.entidad()));
            }

            if (filtro.usuarioId() != null) {
                predicates.add(cb.equal(root.get("usuario").get("id"), filtro.usuarioId()));
            }

            if (filtro.fechaDesde() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaEvento"), filtro.fechaDesde().atStartOfDay().atOffset(ZoneOffset.UTC)));
            }

            if (filtro.fechaHasta() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaEvento"), filtro.fechaHasta().atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}