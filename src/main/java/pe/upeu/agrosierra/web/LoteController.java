package pe.upeu.agrosierra.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.upeu.agrosierra.servicio.LaborService;
import pe.upeu.agrosierra.servicio.LoteService;
import pe.upeu.agrosierra.web.dto.ApiDtos;

@RestController
@RequestMapping("/api/v1/lotes")
public class LoteController {
    private final LoteService lotes;
    private final LaborService labores;
    public LoteController(LoteService lotes, LaborService labores) { this.lotes = lotes; this.labores = labores; }

    @GetMapping
    @PreAuthorize("hasAuthority('LOTE_LEER')")
    public ApiDtos.PaginaResponse<ApiDtos.LoteResponse> listar(
        @RequestParam(defaultValue = "0") int pagina,
        @RequestParam(defaultValue = "20") int tamanio,
        @RequestParam(required = false) String valle,
        @RequestParam(required = false) Boolean campaniaCerrada) {
        return lotes.listar(pagina, tamanio, valle, campaniaCerrada);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('LOTE_LEER')")
    public ApiDtos.LoteResponse detalle(@PathVariable long id) { return lotes.obtener(id); }

    @GetMapping("/{id}/labores")
    @PreAuthorize("hasAuthority('LABOR_LEER')")
    public ApiDtos.PaginaResponse<ApiDtos.LaborResponse> listarLabores(
        @PathVariable long id,
        @RequestParam(defaultValue = "0") int pagina,
        @RequestParam(defaultValue = "20") int tamanio) {
        return labores.listar(id, pagina, tamanio);
    }
}
