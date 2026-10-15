package pe.gob.minedu.sigece.persistence.specs;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import pe.gob.minedu.sigece.dtos.estudiantes.EstudianteFiltroRequestDto;
import pe.gob.minedu.sigece.models.Estudiante;

import java.util.ArrayList;
import java.util.List;

public class EstudianteSpecification {

    public static Specification<Estudiante> conFiltro(EstudianteFiltroRequestDto filtro) {
        return (root, query, cb) -> {
            if (filtro == null) return cb.conjunction();
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.termino() != null && !filtro.termino().isBlank()) {
                String likePattern = "%" + filtro.termino().trim().toLowerCase() + "%";
                Predicate nombres = cb.like(cb.lower(root.get("nombres")), likePattern);
                Predicate apellidos = cb.like(cb.lower(root.get("apellidos")), likePattern);
                Predicate codigo = cb.like(cb.lower(root.get("codigoSiagie")), likePattern);
                predicates.add(cb.or(nombres, apellidos, codigo));
            }

            if (filtro.nivel() != null) {
                predicates.add(cb.equal(root.get("nivel"), filtro.nivel()));
            }

            if (filtro.grado() != null && !filtro.grado().isBlank()) {
                predicates.add(cb.equal(cb.lower(root.get("grado")), filtro.grado().trim().toLowerCase()));
            }

            if (filtro.seccion() != null && !filtro.seccion().isBlank()) {
                predicates.add(cb.equal(cb.upper(root.get("seccion")), filtro.seccion().trim().toUpperCase()));
            }

            if (filtro.activo() != null) {
                predicates.add(cb.equal(root.get("activo"), filtro.activo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}