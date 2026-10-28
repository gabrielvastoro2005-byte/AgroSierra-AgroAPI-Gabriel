package pe.upeu.agrosierra.dominio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.upeu.agrosierra.seguridad.Usuario;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity @Table(name = "labor")
@Getter @Setter @NoArgsConstructor
public class Labor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private TipoLabor tipo;
    @Column(nullable = false) private LocalDate fecha;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "responsable_id", nullable = false)
    private Usuario responsable;
    @Column(nullable = false) private BigDecimal cantidad;
    @Column(nullable = false, length = 500) private String observacion;
    @Column(name = "creado_en", nullable = false) private Instant creadoEn;
}
