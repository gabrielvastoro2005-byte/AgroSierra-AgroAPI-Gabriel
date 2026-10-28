package pe.upeu.agrosierra.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import pe.upeu.agrosierra.dominio.Labor;
import pe.upeu.agrosierra.dominio.Lote;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public final class ApiDtos {
    private ApiDtos() { }

    public record LoginRequest(@NotBlank String usuario, @NotBlank String contrasena) { }
    public record LoginResponse(String token, String usuario, String rol, Set<String> privilegios,
                                long expiraEnEpochMillis) { }
    public record UsuarioActualResponse(String usuario, String rol, Set<String> privilegios) { }
    public record LoteResponse(Long id, String codigo, String nombre, double hectareas,
                               String cultivo, String valle, boolean campaniaCerrada,
                               double dosisMaximaPorHectarea) {
        public static LoteResponse from(Lote lote) {
            return new LoteResponse(lote.getId(), lote.getCodigo(), lote.getNombre(), lote.getHectareas().doubleValue(),
                lote.getCultivo(), lote.getValle(), lote.isCampaniaCerrada(), lote.getDosisMaximaPorHectarea().doubleValue());
        }
    }
    public record LaborWriteRequest(@NotNull Long loteId, @NotNull pe.upeu.agrosierra.dominio.TipoLabor tipo,
                                    @NotNull LocalDate fecha, @Positive double cantidad,
                                    @NotBlank String observacion) { }
    public record LaborUpdateRequest(@NotNull pe.upeu.agrosierra.dominio.TipoLabor tipo,
                                     @NotNull LocalDate fecha, @Positive double cantidad,
                                     @NotBlank String observacion) { }
    public record LaborResponse(Long id, Long loteId, pe.upeu.agrosierra.dominio.TipoLabor tipo,
                                String fecha, String responsable, double cantidad,
                                String observacion, boolean pendiente, long creadoEnEpochMillis) {
        public static LaborResponse from(Labor labor) {
            return new LaborResponse(labor.getId(), labor.getLote().getId(), labor.getTipo(),
                labor.getFecha().toString(), labor.getResponsable().getUsername(), labor.getCantidad().doubleValue(),
                labor.getObservacion(), false, labor.getCreadoEn().toEpochMilli());
        }
    }
    public record PaginaResponse<T>(List<T> contenido, int pagina, int tamanio,
                                    long totalElementos, int totalPaginas, boolean ultima) { }
}
