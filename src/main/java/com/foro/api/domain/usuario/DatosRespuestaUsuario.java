package com.foro.api.domain.usuario;

public record DatosRespuestaUsuario(
        Long id,
        String nombre,
        String email,
        Boolean activo
) {
    public DatosRespuestaUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getActivo()
        );
    }
}