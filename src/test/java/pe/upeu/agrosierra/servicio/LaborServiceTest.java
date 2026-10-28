package pe.upeu.agrosierra.servicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import pe.upeu.agrosierra.dominio.Labor;
import pe.upeu.agrosierra.dominio.Lote;
import pe.upeu.agrosierra.dominio.TipoLabor;
import pe.upeu.agrosierra.repositorio.LaborRepository;
import pe.upeu.agrosierra.repositorio.LoteRepository;
import pe.upeu.agrosierra.repositorio.UsuarioRepository;
import pe.upeu.agrosierra.web.dto.ApiDtos;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LaborServiceTest {
    private LaborRepository labores;
    private LoteRepository lotes;
    private LaborService service;

    @BeforeEach
    void setup() {
        labores = mock(LaborRepository.class);
        lotes = mock(LoteRepository.class);
        service = new LaborService(labores, lotes, mock(UsuarioRepository.class));
    }

    @Test
    void rn01ImpideRegistrarEnCampaniaCerrada() {
        Lote lote = lote(true);
        when(lotes.findById(4L)).thenReturn(Optional.of(lote));
        var request = new ApiDtos.LaborWriteRequest(4L, TipoLabor.RIEGO, LocalDate.now(), 1.0, "Riego");

        var error = assertThrows(ErroresDominio.Conflicto.class, () -> service.crear(request, "tecnico1"));

        assertTrue(error.getMessage().contains("cerrada"));
        verifyNoInteractions(labores);
    }

    @Test
    void rn04Devuelve400ConErrorEnCantidad() {
        when(lotes.findById(1L)).thenReturn(Optional.of(lote(false)));
        var request = new ApiDtos.LaborWriteRequest(1L, TipoLabor.FUMIGACION, LocalDate.now(), 10.0, "Fumigación");

        var error = assertThrows(ErroresDominio.Validacion.class, () -> service.crear(request, "tecnico1"));

        assertEquals("cantidad", error.getCampos().keySet().iterator().next());
    }

    @Test
    void rn03ImpideCerrarSiNoHayCosecha() {
        when(lotes.findById(1L)).thenReturn(Optional.of(lote(false)));
        when(labores.existsByLoteIdAndTipo(1L, TipoLabor.COSECHA)).thenReturn(false);

        var error = assertThrows(ErroresDominio.Conflicto.class, () -> service.cerrarCampania(1L));

        assertTrue(error.getMessage().contains("Cosecha"));
        verify(lotes, never()).save(any());
    }

    @Test
    void rn02TecnicoNoPuedeEditarLaborAjena() {
        Lote lote = lote(false);
        var responsable = new pe.upeu.agrosierra.seguridad.Usuario();
        responsable.setUsername("tecnico1");
        Labor labor = new Labor();
        labor.setId(15L); labor.setLote(lote); labor.setResponsable(responsable);
        labor.setTipo(TipoLabor.RIEGO); labor.setFecha(LocalDate.now()); labor.setCantidad(BigDecimal.valueOf(50));
        labor.setObservacion("Riego"); labor.setCreadoEn(Instant.now());
        when(labores.findById(15L)).thenReturn(Optional.of(labor));

        assertThrows(AccessDeniedException.class, () -> service.actualizar(15L,
            new ApiDtos.LaborUpdateRequest(TipoLabor.RIEGO, LocalDate.now(), 60, "Corrección"),
            "tecnico2", false));
    }

    private Lote lote(boolean closed) {
        Lote lote = new Lote();
        lote.setId(1L); lote.setCodigo("LT-001"); lote.setNombre("El Mirador");
        lote.setHectareas(BigDecimal.valueOf(2.0)); lote.setDosisMaximaPorHectarea(BigDecimal.valueOf(2.0)); lote.setCampaniaCerrada(closed);
        return lote;
    }
}
