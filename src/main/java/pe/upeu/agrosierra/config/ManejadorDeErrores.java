package pe.upeu.agrosierra.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.upeu.agrosierra.servicio.ErroresDominio;
import pe.upeu.agrosierra.web.dto.ErrorResponse;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorDeErrores {
    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ErrorResponse> badCredentials(BadCredentialsException ex, HttpServletRequest req) {
        return response(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos", req, null);
    }
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorResponse> forbidden(AccessDeniedException ex, HttpServletRequest req) {
        return response(HttpStatus.FORBIDDEN, "No posee el privilegio requerido", req, null);
    }
    @ExceptionHandler(ErroresDominio.NoEncontrado.class)
    ResponseEntity<ErrorResponse> notFound(ErroresDominio.NoEncontrado ex, HttpServletRequest req) {
        return response(HttpStatus.NOT_FOUND, ex.getMessage(), req, null);
    }
    @ExceptionHandler(ErroresDominio.Conflicto.class)
    ResponseEntity<ErrorResponse> conflict(ErroresDominio.Conflicto ex, HttpServletRequest req) {
        return response(HttpStatus.CONFLICT, ex.getMessage(), req, null);
    }
    @ExceptionHandler(ErroresDominio.Validacion.class)
    ResponseEntity<ErrorResponse> domainValidation(ErroresDominio.Validacion ex, HttpServletRequest req) {
        return response(HttpStatus.BAD_REQUEST, ex.getMessage(), req, ex.getCampos());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError field : ex.getBindingResult().getFieldErrors()) errors.put(field.getField(), field.getDefaultMessage());
        return response(HttpStatus.BAD_REQUEST, "Datos inválidos", req, errors);
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> unexpected(Exception ex, HttpServletRequest req) {
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno. Intente nuevamente.", req, null);
    }
    private ResponseEntity<ErrorResponse> response(HttpStatus status, String message,
                                                     HttpServletRequest req, Map<String, String> fields) {
        return ResponseEntity.status(status).body(new ErrorResponse(LocalDateTime.now(), status.value(),
            status.getReasonPhrase(), message, req.getRequestURI(), fields));
    }
}
