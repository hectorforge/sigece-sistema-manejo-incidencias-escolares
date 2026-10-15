package pe.gob.minedu.sigece.persistence.specs;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import pe.gob.minedu.sigece.dtos.seguimiento.AccionFiltroRequestDto;
import pe.gob.minedu.sigece.models.AccionSeguimiento;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class AccionSeguimientoSpecification {

    public static Specification<AccionSeguimiento> conFiltro(AccionFiltroRequestDto filtro) {
        return (root, query, cb) -> {
            if (filtro == null) return cb.conjunction();
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.tipoAccion() != null) {
                predicates.add(cb.equal(root.get("tipoAccion"), filtro.tipoAccion()));
            }

            if (filtro.estadoAccion() != null) {
                predicates.add(cb.equal(root.get("estadoAccion"), filtro.estadoAccion()));
            }

            if (filtro.fechaDesde() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaAccion"), filtro.fechaDesde().atStartOfDay().atOffset(ZoneOffset.UTC)));
            }

            if (filtro.fechaHasta() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaAccion"), filtro.fechaHasta().atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}