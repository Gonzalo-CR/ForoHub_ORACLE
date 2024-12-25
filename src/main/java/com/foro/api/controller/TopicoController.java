package com.foro.api.controller;

import com.foro.api.domain.topico.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @PutMapping
    @Transactional
    public ResponseEntity actualizarTopico(@RequestBody @Valid DatosActualizarTopico datos) {
        var topico = topicoRepository.findById(datos.id())
                .orElseThrow(() -> new ValidationException("No fue encontrado un tópico con el ID proporcionado"));

        if (!topico.getActivo()) {
            throw new ValidationException("No fue encontrado un tópico con el ID proporcionado");
        }

        topico.setTitulo(datos.titulo());
        topico.setMensaje(datos.mensaje());
        if (datos.status() != null) {
            topico.setStatus(datos.status());
        }

        return ResponseEntity.ok(new DatosRespuestaTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarTopico(@PathVariable Long id) {
        var topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ValidationException("No fue encontrado un tópico con el ID proporcionado"));

        if (!topico.getActivo()) {
            throw new ValidationException("No fue encontrado un tópico con el ID proporcionado");
        }

        topico.setActivo(false);

        return ResponseEntity.noContent().build();
    }
}