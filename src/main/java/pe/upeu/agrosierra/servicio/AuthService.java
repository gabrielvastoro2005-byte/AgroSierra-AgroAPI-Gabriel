package pe.upeu.agrosierra.servicio;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import pe.upeu.agrosierra.config.JwtService;
import pe.upeu.agrosierra.repositorio.UsuarioRepository;
import pe.upeu.agrosierra.seguridad.Usuario;
import pe.upeu.agrosierra.web.dto.ApiDtos;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository users;
    private final JwtService jwt;
    public AuthService(AuthenticationManager authenticationManager, UsuarioRepository users, JwtService jwt) {
        this.authenticationManager = authenticationManager; this.users = users; this.jwt = jwt;
    }
    public ApiDtos.LoginResponse login(ApiDtos.LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.usuario(), request.contrasena()));
        Usuario user = (Usuario) authentication.getPrincipal();
        String token = jwt.issue(user);
        return new ApiDtos.LoginResponse(token, user.getUsername(), user.getRol().getCodigo(),
            privileges(user), jwt.expiresAtMillis(token));
    }
    public ApiDtos.UsuarioActualResponse me(String username) {
        Usuario user = users.findByUsername(username).orElseThrow(() -> new ErroresDominio.NoEncontrado("Usuario no encontrado"));
        return new ApiDtos.UsuarioActualResponse(user.getUsername(), user.getRol().getCodigo(), privileges(user));
    }
    private Set<String> privileges(Usuario user) {
        return user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
