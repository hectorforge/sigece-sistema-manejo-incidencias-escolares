package pe.gob.minedu.sigece.persistence.specs;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import pe.gob.minedu.sigece.dtos.usuarios.UsuarioFiltroRequestDto;
import pe.gob.minedu.sigece.models.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioSpecification {

    public static Specification<Usuario> conFiltro(UsuarioFiltroRequestDto filtro) {
        return (root, query, cb) -> {
            if (filtro == null) return cb.conjunction();
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.termino() != null && !filtro.termino().isBlank()) {
                String likePattern = "%" + filtro.termino().trim().toLowerCase() + "%";
                Predicate nombre = cb.like(cb.lower(root.get("nombres")), likePattern);
                Predicate apellido = cb.like(cb.lower(root.get("apellidos")), likePattern);
                Predicate username = cb.like(cb.lower(root.get("username")), likePattern);
                Predicate dni = cb.like(root.get("dni"), likePattern);
                predicates.add(cb.or(nombre, apellido, username, dni));
            }

            if (filtro.roleId() != null) {
                predicates.add(cb.equal(root.get("rol").get("id"), filtro.roleId()));
            }

            if (filtro.activo() != null) {
                predicates.add(cb.equal(root.get("activo"), filtro.activo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}