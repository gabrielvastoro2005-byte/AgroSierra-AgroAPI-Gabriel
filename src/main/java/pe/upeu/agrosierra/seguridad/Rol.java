package pe.upeu.agrosierra.seguridad;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rol")
@Getter @Setter @NoArgsConstructor
public class Rol {
    @Id
    private String codigo;

    private String descripcion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "rol_privilegio",
        joinColumns = @JoinColumn(name = "rol_codigo"),
        inverseJoinColumns = @JoinColumn(name = "privilegio_codigo"))
    private Set<Privilegio> privilegios = new HashSet<>();
}
