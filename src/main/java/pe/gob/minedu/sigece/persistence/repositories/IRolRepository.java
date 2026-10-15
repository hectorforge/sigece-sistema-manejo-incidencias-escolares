package pe.gob.minedu.sigece.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.gob.minedu.sigece.models.Rol;

import java.util.Optional;

@Repository
public interface IRolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}