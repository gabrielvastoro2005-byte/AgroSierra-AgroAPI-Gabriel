package pe.upeu.agrosierra.seguridad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "privilegio")
@Getter @Setter @NoArgsConstructor
public class Privilegio {
    @Id
    @Column(name = "codigo", length = 80)
    private String codigo;

    @Column(nullable = false, length = 200)
    private String descripcion;
}
