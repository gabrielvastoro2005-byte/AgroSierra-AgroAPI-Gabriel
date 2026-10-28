package pe.upeu.agrosierra.repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.upeu.agrosierra.dominio.Labor;
import pe.upeu.agrosierra.dominio.TipoLabor;

public interface LaborRepository extends JpaRepository<Labor, Long> {
    Page<Labor> findByLoteIdOrderByFechaDescIdDesc(Long loteId, Pageable pageable);
    boolean existsByLoteIdAndTipo(Long loteId, TipoLabor tipo);
}
