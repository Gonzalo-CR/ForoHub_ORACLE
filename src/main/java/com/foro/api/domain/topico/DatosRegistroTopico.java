package com.foro.api.domain.topico;

public record DatosRegistroTopico(
        String titulo,
        String mensaje,
        Long autorId,
        Long cursoId
) {}
