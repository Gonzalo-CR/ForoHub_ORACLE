package com.foro.api.controller;

import com.foro.api.domain.respuesta.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/respuestas")
@Tag(name = "Respuestas", description = "Gestión de respuestas del foro")
public class RespuestaController {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @PostMapping
    @Transactional
    @Operation(summary = "Registra una nueva respuesta")
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroRespuesta datos,
                                    UriComponentsBuilder uriBuilder) {
        var respuesta = new Respuesta(datos);
        respuestaRepository.save(respuesta);

        var uri = uriBuilder.path("/respuestas/{id}").buildAndExpand(respuesta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosRespuestaRespuesta(respuesta));
    }

    @GetMapping
    @Operation(summary = "Lista todas las respuestas de un tópico")
    public ResponseEntity<List<DatosRespuestaRespuesta>> listar(@RequestParam Long topicoId) {
        var respuestas = respuestaRepository.findByTopicoIdAndActivoTrue(topicoId)
                .stream().map(DatosRespuestaRespuesta::new).toList();
        return ResponseEntity.ok(respuestas);
    }
}