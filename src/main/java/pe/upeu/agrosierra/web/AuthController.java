package pe.upeu.agrosierra.web;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.upeu.agrosierra.servicio.AuthService;
import pe.upeu.agrosierra.web.dto.ApiDtos;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/auth/login")
    public ApiDtos.LoginResponse login(@Valid @RequestBody ApiDtos.LoginRequest request) {
        return auth.login(request);
    }

    @GetMapping("/usuarios/me")
    public ApiDtos.UsuarioActualResponse me(Authentication authentication) {
        return auth.me(authentication.getName());
    }
}
