package pe.gob.minedu.sigece.models;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Catálogo de tipos de incidencia de convivencia escolar
 * (violencia física, psicológica, sexual, bullying, ciberacoso, etc.),
 * mapeado a las categorías del Sistema SíSeVe del MINEDU.
 */
@Entity
@Table(name = "tipos_incidencia")
public class TipoIncidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "categoria_siseve", length = 50)
    private String categoriaSiseve;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    protected TipoIncidencia() {
        // Requerido por JPA
    }

    public TipoIncidencia(String nombre, String categoriaSiseve, String descripcion) {
        this.nombre = nombre;
        this.categoriaSiseve = categoriaSiseve;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoriaSiseve() {
        return categoriaSiseve;
    }

    public void setCategoriaSiseve(String categoriaSiseve) {
        this.categoriaSiseve = categoriaSiseve;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TipoIncidencia)) return false;
        TipoIncidencia that = (TipoIncidencia) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TipoIncidencia{id=" + id + ", nombre='" + nombre + "'}";
    }
}
