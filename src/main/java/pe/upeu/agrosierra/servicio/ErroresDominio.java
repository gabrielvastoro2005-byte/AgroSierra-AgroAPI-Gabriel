package pe.upeu.agrosierra.servicio;

import java.util.Map;

public final class ErroresDominio {
    private ErroresDominio() { }
    public static class NoEncontrado extends RuntimeException {
        public NoEncontrado(String mensaje) { super(mensaje); }
    }
    public static class Conflicto extends RuntimeException {
        public Conflicto(String mensaje) { super(mensaje); }
    }
    public static class Validacion extends RuntimeException {
        private final Map<String, String> campos;
        public Validacion(String campo, String mensaje) { super("Datos inválidos"); this.campos = Map.of(campo, mensaje); }
        public Map<String, String> getCampos() { return campos; }
    }
}
