package com.foro.api.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DatosAutenticacionUsuario(@NotBlank(message = "El login no puede estar vacío") String login,
                                        @NotBlank(message = "La clave no puede estar vacía") String password) {
    public String clave() {
        return password;
    }
}
