package pe.upeu.agrosierra.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.upeu.agrosierra.servicio.LaborService;
import pe.upeu.agrosierra.web.dto.ApiDtos;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class LaborController {
    private final LaborService labores;
    public LaborController(LaborService labores) { this.labores = labores; }

    @PostMapping("/labores")
    @PreAuthorize("hasAuthority('LABOR_CREAR')")
    public ResponseEntity<ApiDtos.LaborResponse> crear(@Valid @RequestBody ApiDtos.LaborWriteRequest request,
                                                       Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labores.crear(request, authentication.getName()));
    }

    @PutMapping("/labores/{id}")
    @PreAuthorize("hasAuthority('LABOR_EDITAR')")
    public ApiDtos.LaborResponse actualizar(@PathVariable long id,
        @Valid @RequestBody ApiDtos.LaborUpdateRequest request, Authentication authentication) {
        Set<String> permissions = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return labores.actualizar(id, request, authentication.getName(), permissions.contains("LABOR_EDITAR_TODAS"));
    }

    @DeleteMapping("/labores/{id}")
    @PreAuthorize("hasAuthority('LABOR_ELIMINAR')")
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        labores.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lotes/{id}/cerrar-campania")
    @PreAuthorize("hasAuthority('CAMPANIA_CERRAR')")
    public ResponseEntity<Void> cerrarCampania(@PathVariable long id) {
        labores.cerrarCampania(id);
        return ResponseEntity.ok().build();
    }
}
