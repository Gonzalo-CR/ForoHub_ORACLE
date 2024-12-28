package com.foro.api.controller;

import com.foro.api.domain.respuesta.*;
import com.foro.api.service.RespuestaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import com.foro.api.domain.respuesta.DatosRespuestaRespuesta;

@RestController
@RequestMapping("/respuestas")
@Tag(name = "Respuestas", description = "Gestión de respuestas del foro")
public class RespuestaController {

    @Autowired
    private RespuestaService respuestaService;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @PostMapping
    @Transactional
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroRespuesta datos,
                                    UriComponentsBuilder uriBuilder) {
        var respuesta = respuestaService.crearRespuesta(datos);
        respuestaRepository.save(respuesta);

        var uri = uriBuilder.path("/respuestas/{id}").buildAndExpand(respuesta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DatosRespuestaRespuesta(respuesta));
    }

    @GetMapping
    @Operation(summary = "Lista todas las respuestas de un tópico")
    public ResponseEntity<List<DatosRespuestaRespuesta>> listar(@RequestParam(name = "topico_id") Long topicoId) {
        List<Respuesta> respuestas = respuestaRepository.findByTopicoIdAndActivoTrue(topicoId);

        if (respuestas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        var datosRespuestas = respuestas.stream()
                .map(DatosRespuestaRespuesta::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(datosRespuestas);
    }
}