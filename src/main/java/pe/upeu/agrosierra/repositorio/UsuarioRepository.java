package pe.upeu.agrosierra.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.upeu.agrosierra.seguridad.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
