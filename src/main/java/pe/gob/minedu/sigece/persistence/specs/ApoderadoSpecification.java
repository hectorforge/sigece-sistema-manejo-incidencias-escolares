package pe.gob.minedu.sigece.persistence.specs;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import pe.gob.minedu.sigece.dtos.apoderados.ApoderadoFiltroRequestDto;
import pe.gob.minedu.sigece.models.Apoderado;

import java.util.ArrayList;
import java.util.List;

public class ApoderadoSpecification {

    public static Specification<Apoderado> conFiltro(ApoderadoFiltroRequestDto filtro) {
        return (root, query, cb) -> {
            if (filtro == null) return cb.conjunction();
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.termino() != null && !filtro.termino().isBlank()) {
                String pattern = "%" + filtro.termino().trim().toLowerCase() + "%";
                Predicate nombres = cb.like(cb.lower(root.get("nombres")), pattern);
                Predicate apellidos = cb.like(cb.lower(root.get("apellidos")), pattern);
                Predicate doc = cb.like(cb.lower(root.get("numeroDocumento")), pattern);
                predicates.add(cb.or(nombres, apellidos, doc));
            }

            if (filtro.tipoDocumento() != null) {
                predicates.add(cb.equal(root.get("tipoDocumento"), filtro.tipoDocumento()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}