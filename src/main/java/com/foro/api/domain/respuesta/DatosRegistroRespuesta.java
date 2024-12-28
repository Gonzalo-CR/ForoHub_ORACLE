package com.foro.api.domain.respuesta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroRespuesta(
        @NotBlank(message = "El mensaje no puede estar vacío")
        String mensaje,

        @NotNull(message = "El ID del tópico es obligatorio")
        Long topicoId,

        @NotNull(message = "El ID del autor es obligatorio")
        Long autorId,

        Boolean solucion
) {
    public DatosRegistroRespuesta {
        // Constructor compacto que permite validaciones adicionales si son necesarias
        if (solucion == null) {
            solucion = false;
        }
    }
}