package pe.upeu.agrosierra.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.upeu.agrosierra.seguridad.Rol;

public interface RolRepository extends JpaRepository<Rol, String> { }
