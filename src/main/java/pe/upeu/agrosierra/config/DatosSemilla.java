package pe.upeu.agrosierra.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.upeu.agrosierra.dominio.Labor;
import pe.upeu.agrosierra.dominio.Lote;
import pe.upeu.agrosierra.dominio.TipoLabor;
import pe.upeu.agrosierra.repositorio.LaborRepository;
import pe.upeu.agrosierra.repositorio.LoteRepository;
import pe.upeu.agrosierra.repositorio.RolRepository;
import pe.upeu.agrosierra.repositorio.UsuarioRepository;
import pe.upeu.agrosierra.seguridad.Usuario;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class DatosSemilla {
    @Bean
    ApplicationRunner seed(RolRepository roles, UsuarioRepository users, LoteRepository lots,
                           LaborRepository labors, PasswordEncoder encoder) {
        return args -> {
            if (users.count() > 0) return;
            Usuario tecnico = user("tecnico1", "TECNICO", roles, encoder);
            Usuario jefe = user("jefe1", "JEFE_CAMPO", roles, encoder);
            Usuario admin = user("admin", "ADMINISTRADOR", roles, encoder);
            users.saveAll(List.of(tecnico, jefe, admin));
            Lote[] lote = new Lote[8];
            lote[0] = lot("LT-001", "El Mirador", 4.5, "Café", "Chanchamayo", false, 2.0);
            lote[1] = lot("LT-002", "La Esperanza", 3.2, "Cacao", "Chanchamayo", false, 1.5);
            lote[2] = lot("LT-003", "Valle Verde", 2.8, "Maíz", "Mantaro", false, 2.5);
            lote[3] = lot("LT-004", "La Cosecha", 5.0, "Papa", "Mantaro", true, 3.0);
            lote[4] = lot("LT-005", "Los Pinos", 6.4, "Café", "Satipo", false, 1.8);
            lote[5] = lot("LT-006", "Río Claro", 3.7, "Cacao", "Satipo", false, 1.6);
            lote[6] = lot("LT-007", "Las Lomas", 2.4, "Maíz", "Chanchamayo", true, 2.2);
            lote[7] = lot("LT-008", "Nueva Vida", 4.1, "Papa", "Mantaro", false, 2.7);
            lots.saveAll(List.of(lote));
            laborSeed(labors, lote[0], tecnico, TipoLabor.RIEGO, 2, 18, 150);
            laborSeed(labors, lote[0], tecnico, TipoLabor.FUMIGACION, 1, 18, 5);
            laborSeed(labors, lote[0], jefe, TipoLabor.COSECHA, 0, 12, 900);
            laborSeed(labors, lote[1], tecnico, TipoLabor.RIEGO, 3, 17, 130);
            laborSeed(labors, lote[1], jefe, TipoLabor.COSECHA, 0, 11, 640);
            laborSeed(labors, lote[1], tecnico, TipoLabor.FUMIGACION, 2, 10, 4);
            laborSeed(labors, lote[2], tecnico, TipoLabor.RIEGO, 3, 15, 90);
            laborSeed(labors, lote[2], tecnico, TipoLabor.FUMIGACION, 1, 14, 3);
            laborSeed(labors, lote[3], jefe, TipoLabor.COSECHA, 0, 80, 1100);
            laborSeed(labors, lote[3], jefe, TipoLabor.RIEGO, 1, 79, 200);
            laborSeed(labors, lote[4], tecnico, TipoLabor.RIEGO, 2, 8, 230);
            laborSeed(labors, lote[4], jefe, TipoLabor.COSECHA, 0, 5, 1500);
            laborSeed(labors, lote[4], tecnico, TipoLabor.FUMIGACION, 1, 4, 6);
            laborSeed(labors, lote[5], tecnico, TipoLabor.RIEGO, 1, 4, 120);
            laborSeed(labors, lote[5], jefe, TipoLabor.COSECHA, 0, 3, 520);
            laborSeed(labors, lote[6], jefe, TipoLabor.COSECHA, 0, 90, 730);
            laborSeed(labors, lote[6], jefe, TipoLabor.RIEGO, 1, 89, 100);
            laborSeed(labors, lote[7], tecnico, TipoLabor.RIEGO, 2, 3, 160);
            laborSeed(labors, lote[7], jefe, TipoLabor.COSECHA, 0, 2, 820);
            laborSeed(labors, lote[7], tecnico, TipoLabor.FUMIGACION, 1, 1, 5);
        };
    }

    private Usuario user(String username, String role, RolRepository roles, PasswordEncoder encoder) {
        Usuario user = new Usuario(); user.setUsername(username); user.setPasswordHash(encoder.encode("Agro2026*"));
        user.setRol(roles.findById(role).orElseThrow()); user.setActivo(true); return user;
    }
    private Lote lot(String codigo, String nombre, double hectares, String crop, String valley,
                     boolean closed, double maxDose) {
        Lote lote = new Lote(); lote.setCodigo(codigo); lote.setNombre(nombre); lote.setHectareas(BigDecimal.valueOf(hectares));
        lote.setCultivo(crop); lote.setValle(valley); lote.setCampaniaCerrada(closed);
        lote.setDosisMaximaPorHectarea(BigDecimal.valueOf(maxDose)); return lote;
    }
    private void laborSeed(LaborRepository repository, Lote lote, Usuario owner, TipoLabor type,
                           int daysAgo, int hoursAgo, double amount) {
        Labor labor = new Labor(); labor.setLote(lote); labor.setResponsable(owner); labor.setTipo(type);
        labor.setFecha(LocalDate.now().minusDays(daysAgo)); labor.setCantidad(BigDecimal.valueOf(amount));
        labor.setObservacion("Registro semilla para demostración");
        labor.setCreadoEn(Instant.now().minusSeconds(daysAgo * 86400L + hoursAgo * 3600L)); repository.save(labor);
    }
}
