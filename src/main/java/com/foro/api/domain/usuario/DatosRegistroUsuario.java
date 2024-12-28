package com.foro.api.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DatosRegistroUsuario(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El login es obligatorio")
        @Size(min = 4, max = 20, message = "El login debe tener entre 4 y 20 caracteres")
        String login,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email es inválido")
        String email,

        @NotBlank(message = "El password es obligatorio")
        @Size(min = 8, message = "El password debe tener al menos 8 caracteres")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = """
                        La contraseña debe cumplir los siguientes requisitos:
                        - Al menos una letra mayúscula
                        - Al menos una letra minúscula
                        - Al menos un número
                        - Al menos un carácter especial
                        - Sin espacios en blanco
                        """
        )
        String password
) {}