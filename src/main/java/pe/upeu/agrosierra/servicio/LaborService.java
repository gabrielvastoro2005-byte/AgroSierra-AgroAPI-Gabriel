package pe.upeu.agrosierra.servicio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upeu.agrosierra.dominio.Labor;
import pe.upeu.agrosierra.dominio.Lote;
import pe.upeu.agrosierra.dominio.TipoLabor;
import pe.upeu.agrosierra.repositorio.LaborRepository;
import pe.upeu.agrosierra.repositorio.LoteRepository;
import pe.upeu.agrosierra.repositorio.UsuarioRepository;
import pe.upeu.agrosierra.web.dto.ApiDtos;
import java.math.BigDecimal;
import java.time.Instant;

@Service
public class LaborService {
    private final LaborRepository labores;
    private final LoteRepository lotes;
    private final UsuarioRepository usuarios;

    public LaborService(LaborRepository labores, LoteRepository lotes, UsuarioRepository usuarios) {
        this.labores = labores; this.lotes = lotes; this.usuarios = usuarios;
    }

    @Transactional(readOnly = true)
    public ApiDtos.PaginaResponse<ApiDtos.LaborResponse> listar(long loteId, int pagina, int tamanio) {
        if (pagina < 0 || tamanio < 1 || tamanio > 100)
            throw new ErroresDominio.Validacion("tamanio", "Página inválida; tamaño permitido entre 1 y 100");
        if (!lotes.existsById(loteId)) throw new ErroresDominio.NoEncontrado("Lote no encontrado");
        Page<ApiDtos.LaborResponse> page = labores.findByLoteIdOrderByFechaDescIdDesc(
            loteId, PageRequest.of(pagina, tamanio, Sort.unsorted())).map(ApiDtos.LaborResponse::from);
        return new ApiDtos.PaginaResponse<>(page.getContent(), page.getNumber(), page.getSize(),
            page.getTotalElements(), page.getTotalPages(), page.isLast());
    }

    @Transactional
    public ApiDtos.LaborResponse crear(ApiDtos.LaborWriteRequest request, String username) {
        Lote lote = obtenerLote(request.loteId());
        comprobarCampaniaAbierta(lote);
        comprobarDosis(lote, request.tipo(), request.cantidad());
        Labor labor = new Labor();
        labor.setLote(lote);
        labor.setTipo(request.tipo());
        labor.setFecha(request.fecha());
        labor.setResponsable(obtenerUsuario(username));
        labor.setCantidad(BigDecimal.valueOf(request.cantidad()));
        labor.setObservacion(request.observacion().trim());
        labor.setCreadoEn(Instant.now());
        return ApiDtos.LaborResponse.from(labores.save(labor));
    }

    @Transactional
    public ApiDtos.LaborResponse actualizar(long id, ApiDtos.LaborUpdateRequest request, String username,
                                            boolean puedeEditarTodas) {
        Labor labor = labores.findById(id).orElseThrow(() -> new ErroresDominio.NoEncontrado("Labor no encontrada"));
        comprobarCampaniaAbierta(labor.getLote());
        if (!puedeEditarTodas) {
            boolean propia = labor.getResponsable().getUsername().equals(username);
            boolean dentroDePlazo = Instant.now().isBefore(labor.getCreadoEn().plusSeconds(24 * 60 * 60L));
            if (!propia || !dentroDePlazo)
                throw new org.springframework.security.access.AccessDeniedException(
                    "El técnico solo puede corregir sus propias labores durante las primeras 24 horas");
        }
        comprobarDosis(labor.getLote(), request.tipo(), request.cantidad());
        labor.setTipo(request.tipo());
        labor.setFecha(request.fecha());
        labor.setCantidad(BigDecimal.valueOf(request.cantidad()));
        labor.setObservacion(request.observacion().trim());
        return ApiDtos.LaborResponse.from(labores.save(labor));
    }

    @Transactional
    public void eliminar(long id) {
        if (!labores.existsById(id)) throw new ErroresDominio.NoEncontrado("Labor no encontrada");
        labores.deleteById(id);
    }

    @Transactional
    public void cerrarCampania(long loteId) {
        Lote lote = obtenerLote(loteId);
        if (!labores.existsByLoteIdAndTipo(loteId, TipoLabor.COSECHA))
            throw new ErroresDominio.Conflicto("No se puede cerrar la campaña: el lote aún no registra una labor de Cosecha");
        lote.setCampaniaCerrada(true);
        lotes.save(lote);
    }

    private Lote obtenerLote(long id) {
        return lotes.findById(id).orElseThrow(() -> new ErroresDominio.NoEncontrado("Lote no encontrado"));
    }
    private pe.upeu.agrosierra.seguridad.Usuario obtenerUsuario(String username) {
        return usuarios.findByUsername(username).orElseThrow(() -> new ErroresDominio.NoEncontrado("Usuario no encontrado"));
    }
    private void comprobarCampaniaAbierta(Lote lote) {
        if (lote.isCampaniaCerrada()) throw new ErroresDominio.Conflicto("No se puede registrar ni corregir: la campaña del lote está cerrada");
    }
    private void comprobarDosis(Lote lote, TipoLabor tipo, double cantidad) {
        if (tipo == TipoLabor.FUMIGACION) {
            BigDecimal maximo = lote.getDosisMaximaPorHectarea().multiply(lote.getHectareas());
            if (BigDecimal.valueOf(cantidad).compareTo(maximo) > 0) throw new ErroresDominio.Validacion("cantidad",
                "La dosis máxima para este lote es " + maximo + " (dosis por hectárea × hectáreas)");
        }
    }
}
