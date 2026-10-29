package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.gob.minedu.sigece.enums.RolNombre;
import pe.gob.minedu.sigece.models.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByDni(String dni);

    boolean existsByUsername(String username);

    boolean existsByCorreo(String correo);

    boolean existsByDni(String dni);

    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = :rolNombre AND u.activo = true")
    List<Usuario> findAllByRolNombreAndActivoTrue(@Param("rolNombre") String rolNombre);

    @Query("SELECT COUNT(i) FROM Incidente i WHERE i.responsableActual.id = :usuarioId AND i.estado <> pe.gob.minedu.sigece.enums.EstadoIncidente.CERRADO")
    int countCasosActivosAsignados(@Param("usuarioId") Long usuarioId);


    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = :rol AND u.activo = true")
    List<Usuario> findAllByRolNombreAndActivoTrue(@Param("rol") RolNombre rol);
}
