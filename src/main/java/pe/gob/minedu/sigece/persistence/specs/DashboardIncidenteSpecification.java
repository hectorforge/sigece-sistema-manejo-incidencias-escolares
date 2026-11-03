package pe.gob.minedu.sigece.persistence.specs;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import pe.gob.minedu.sigece.dtos.dashboard.DashboardFiltroRequestDto;
import pe.gob.minedu.sigece.models.Estudiante;
import pe.gob.minedu.sigece.models.Incidente;
import pe.gob.minedu.sigece.models.IncidenteInvolucrado;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class DashboardIncidenteSpecification {

    public static Specification<Incidente> conFiltro(DashboardFiltroRequestDto filtro) {
        return (root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();

            if (filtro != null) {
                // Rango de fecha por fechaOcurrencia
                if (filtro.fechaDesde() != null) {
                    ps.add(cb.greaterThanOrEqualTo(
                            root.get("fechaOcurrencia"),
                            filtro.fechaDesde().atStartOfDay().atOffset(ZoneOffset.UTC)
                    ));
                }

                if (filtro.fechaHasta() != null) {
                    ps.add(cb.lessThanOrEqualTo(
                            root.get("fechaOcurrencia"),
                            filtro.fechaHasta().atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC)
                    ));
                }

                // Filtro por Nivel y/o Grado evaluando los estudiantes involucrados
                if (filtro.nivel() != null || (filtro.grado() != null && !filtro.grado().isBlank())) {
                    Subquery<Long> sub = query.subquery(Long.class);
                    Root<IncidenteInvolucrado> ii = sub.from(IncidenteInvolucrado.class);
                    Join<IncidenteInvolucrado, Estudiante> est = ii.join("estudiante");

                    List<Predicate> subPreds = new ArrayList<>();
                    subPreds.add(cb.equal(ii.get("incidente"), root));

                    if (filtro.nivel() != null) {
                        subPreds.add(cb.equal(est.get("nivel"), filtro.nivel()));
                    }

                    if (filtro.grado() != null && !filtro.grado().isBlank()) {
                        subPreds.add(cb.equal(est.get("grado"), filtro.grado().trim()));
                    }

                    sub.select(ii.get("id")).where(subPreds.toArray(new Predicate[0]));
                    ps.add(cb.exists(sub));
                }
            }

            return cb.and(ps.toArray(new Predicate[0]));
        };
    }
}