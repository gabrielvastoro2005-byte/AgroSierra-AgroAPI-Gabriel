package pe.upeu.agrosierra.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity @Table(name = "lote")
@Getter @Setter @NoArgsConstructor
public class Lote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 30) private String codigo;
    @Column(nullable = false, length = 120) private String nombre;
    @Column(nullable = false) private BigDecimal hectareas;
    @Column(nullable = false, length = 80) private String cultivo;
    @Column(nullable = false, length = 80) private String valle;
    @Convert(converter = BooleanoAEnteroConverter.class)
    @Column(name = "campania_cerrada", nullable = false) private boolean campaniaCerrada;
    @Column(name = "dosis_maxima", nullable = false) private BigDecimal dosisMaximaPorHectarea;
}
