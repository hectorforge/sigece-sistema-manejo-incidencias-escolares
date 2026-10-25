package pe.gob.minedu.sigece.persistence.specs;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;
import pe.gob.minedu.sigece.dtos.incidentes.IncidenteFiltroRequestDto;
import pe.gob.minedu.sigece.models.Estudiante;
import pe.gob.minedu.sigece.models.Incidente;
import pe.gob.minedu.sigece.models.IncidenteInvolucrado;
import pe.gob.minedu.sigece.models.TutorAula;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class IncidenteSpecification {

    /** @param aulasTutor null = sin restricción (ADMIN/PSICOLOGO); lista = solo esas aulas (TUTOR) */
    public static Specification<Incidente> conFiltro(IncidenteFiltroRequestDto filtro, List<TutorAula> aulasTutor) {
        return (root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();

            if (filtro != null) {
                if (filtro.termino() != null && !filtro.termino().isBlank()) {
                    String p = "%" + filtro.termino().trim().toLowerCase() + "%";
                    ps.add(cb.or(
                            cb.like(cb.lower(root.get("codigoCaso")), p),
                            cb.like(cb.lower(root.get("lugarOcurrencia")), p),
                            cb.like(cb.lower(root.get("descripcion")), p)));
                }
                if (filtro.estado() != null) ps.add(cb.equal(root.get("estado"), filtro.estado()));
                if (filtro.gravedad() != null) ps.add(cb.equal(root.get("gravedad"), filtro.gravedad()));
                if (filtro.tipoIncidenciaId() != null)
                    ps.add(cb.equal(root.get("tipoIncidencia").get("id"), filtro.tipoIncidenciaId()));
                if (filtro.fechaDesde() != null)
                    ps.add(cb.greaterThanOrEqualTo(root.get("fechaOcurrencia"),
                            filtro.fechaDesde().atStartOfDay().atOffset(ZoneOffset.UTC)));
                if (filtro.fechaHasta() != null)
                    ps.add(cb.lessThanOrEqualTo(root.get("fechaOcurrencia"),
                            filtro.fechaHasta().atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC)));

                if (filtro.reincidencia() != null) {
                    // Reincidencia = algún estudiante del caso figura también en otro incidente
                    Subquery<Long> sub = query.subquery(Long.class);
                    Root<IncidenteInvolucrado> a = sub.from(IncidenteInvolucrado.class);
                    Root<IncidenteInvolucrado> b = sub.from(IncidenteInvolucrado.class);
                    sub.select(a.get("id")).where(
                            cb.equal(a.get("incidente"), root),
                            cb.isNotNull(a.get("estudiante")),
                            cb.equal(b.get("estudiante"), a.get("estudiante")),
                            cb.notEqual(b.get("incidente"), root));
                    ps.add(filtro.reincidencia() ? cb.exists(sub) : cb.not(cb.exists(sub)));
                }
            }

            if (aulasTutor != null) {
                if (aulasTutor.isEmpty()) {
                    ps.add(cb.disjunction());
                } else {
                    Subquery<Long> sub = query.subquery(Long.class);
                    Root<IncidenteInvolucrado> ii = sub.from(IncidenteInvolucrado.class);
                    Join<IncidenteInvolucrado, Estudiante> est = ii.join("estudiante");
                    List<Predicate> aulas = aulasTutor.stream().map(t -> cb.and(
                            cb.equal(est.get("nivel"), t.getNivel()),
                            cb.equal(est.get("grado"), t.getGrado()),
                            cb.equal(est.get("seccion"), t.getSeccion()))).toList();
                    sub.select(ii.get("id")).where(
                            cb.equal(ii.get("incidente"), root),
                            cb.or(aulas.toArray(new Predicate[0])));
                    ps.add(cb.exists(sub));
                }
            }
            return cb.and(ps.toArray(new Predicate[0]));
        };
    }
}