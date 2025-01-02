package com.foro.api.controller;

import com.foro.api.domain.usuario.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Usuarios", description = "Gestión de usuarios del foro")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    @Operation(summary = "Registra un nuevo usuario")
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroUsuario datosRegistroUsuario,
                                    UriComponentsBuilder uriBuilder) {
        var usuario = new Usuario(datosRegistroUsuario);
        usuario.setPassword(passwordEncoder.encode(datosRegistroUsuario.password()));
        usuarioRepository.save(usuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosRespuestaUsuario(usuario));
    }

    @GetMapping
    @Operation(summary = "Lista todos los usuarios")
    public ResponseEntity<Page<DatosRespuestaUsuario>> listar(Pageable paginacion) {
        var usuarios = usuarioRepository.findByActivoTrue(paginacion)
                .map(DatosRespuestaUsuario::new);
        return ResponseEntity.ok(usuarios);
    }
}