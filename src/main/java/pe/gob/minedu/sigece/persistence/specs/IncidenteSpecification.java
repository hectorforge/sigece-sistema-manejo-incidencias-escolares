package pe.gob.minedu.sigece.persistence.specs;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import pe.gob.minedu.sigece.dtos.incidentes.IncidenteFiltroRequestDto;
import pe.gob.minedu.sigece.models.Incidente;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class IncidenteSpecification {

    public static Specification<Incidente> conFiltro(IncidenteFiltroRequestDto filtro) {
        return (root, query, cb) -> {
            if (filtro == null) return cb.conjunction();
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.termino() != null && !filtro.termino().isBlank()) {
                String pattern = "%" + filtro.termino().trim().toLowerCase() + "%";
                Predicate codigo = cb.like(cb.lower(root.get("codigoCaso")), pattern);
                Predicate lugar = cb.like(cb.lower(root.get("lugarOcurrencia")), pattern);
                Predicate desc = cb.like(cb.lower(root.get("descripcion")), pattern);
                predicates.add(cb.or(codigo, lugar, desc));
            }

            if (filtro.estado() != null) {
                predicates.add(cb.equal(root.get("estado"), filtro.estado()));
            }

            if (filtro.gravedad() != null) {
                predicates.add(cb.equal(root.get("gravedad"), filtro.gravedad()));
            }

            if (filtro.tipoIncidenciaId() != null) {
                predicates.add(cb.equal(root.get("tipoIncidencia").get("id"), filtro.tipoIncidenciaId()));
            }

            if (filtro.reincidencia() != null) {
                predicates.add(cb.equal(root.get("reincidencia"), filtro.reincidencia()));
            }

            if (filtro.fechaDesde() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaOcurrencia"), filtro.fechaDesde().atStartOfDay().atOffset(ZoneOffset.UTC)));
            }

            if (filtro.fechaHasta() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaOcurrencia"), filtro.fechaHasta().atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}