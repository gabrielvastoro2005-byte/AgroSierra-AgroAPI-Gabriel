package pe.upeu.agrosierra.servicio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upeu.agrosierra.dominio.Lote;
import pe.upeu.agrosierra.repositorio.LoteRepository;
import pe.upeu.agrosierra.web.dto.ApiDtos;

@Service
public class LoteService {
    private final LoteRepository repository;
    public LoteService(LoteRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    public ApiDtos.PaginaResponse<ApiDtos.LoteResponse> listar(int pagina, int tamanio,
                                                                 String valle, Boolean campaniaCerrada) {
        validarPagina(pagina, tamanio);
        Specification<Lote> filtro = (root, query, cb) -> cb.conjunction();
        if (valle != null && !valle.isBlank())
            filtro = filtro.and((root, query, cb) -> cb.equal(cb.lower(root.get("valle")), valle.toLowerCase()));
        if (campaniaCerrada != null)
            filtro = filtro.and((root, query, cb) -> cb.equal(root.get("campaniaCerrada"), campaniaCerrada));
        Page<ApiDtos.LoteResponse> page = repository.findAll(filtro,
                PageRequest.of(pagina, tamanio, Sort.by("valle").ascending().and(Sort.by("nombre").ascending())))
            .map(ApiDtos.LoteResponse::from);
        return page(page);
    }

    @Transactional(readOnly = true)
    public ApiDtos.LoteResponse obtener(long id) {
        return ApiDtos.LoteResponse.from(repository.findById(id)
            .orElseThrow(() -> new ErroresDominio.NoEncontrado("Lote no encontrado")));
    }

    private void validarPagina(int pagina, int tamanio) {
        if (pagina < 0) throw new ErroresDominio.Validacion("pagina", "La página debe ser 0 o mayor");
        if (tamanio < 1 || tamanio > 100) throw new ErroresDominio.Validacion("tamanio", "Use un tamaño entre 1 y 100");
    }
    private <T> ApiDtos.PaginaResponse<T> page(Page<T> page) {
        return new ApiDtos.PaginaResponse<>(page.getContent(), page.getNumber(), page.getSize(),
            page.getTotalElements(), page.getTotalPages(), page.isLast());
    }
}
