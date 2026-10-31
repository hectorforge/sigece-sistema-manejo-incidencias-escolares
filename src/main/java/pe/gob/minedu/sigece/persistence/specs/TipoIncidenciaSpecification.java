package pe.gob.minedu.sigece.persistence.specs;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import pe.gob.minedu.sigece.dtos.tiposincidencia.TipoInicidenciaFiltroRequestDto;
import pe.gob.minedu.sigece.models.TipoIncidencia;

import java.util.ArrayList;
import java.util.List;

public class TipoIncidenciaSpecification {

    public static Specification<TipoIncidencia> conFiltro(TipoInicidenciaFiltroRequestDto filtro) {
        return (root, query, cb) -> {
            if (filtro == null) return cb.conjunction();
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.termino() != null && !filtro.termino().isBlank()) {
                String pattern = "%" + filtro.termino().trim().toLowerCase() + "%";
                Predicate nombre = cb.like(cb.lower(root.get("nombre")), pattern);
                Predicate descripcion = cb.like(cb.lower(root.get("descripcion")), pattern);
                predicates.add(cb.or(nombre, descripcion));
            }

            if (filtro.categoriaSiseve() != null) {
                predicates.add(cb.equal(root.get("categoriaSiseve"), filtro.categoriaSiseve()));
            }

            if (filtro.gravedadBase() != null) {
                predicates.add(cb.equal(root.get("gravedadBase"), filtro.gravedadBase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
