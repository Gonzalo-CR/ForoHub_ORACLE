package com.foro.api.controller;

import com.foro.api.domain.topico.*;
import com.foro.api.infra.errores.ValidationException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(
            @RequestBody @Valid DatosRegistroTopico datos,
            UriComponentsBuilder uriComponentsBuilder) {

        Topico topico = new Topico(datos);
        topicoRepository.save(topico);

        var uri = uriComponentsBuilder.path("/topicos/{id}")
                .buildAndExpand(topico.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new DatosRespuestaTopico(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listadoTopicos(
            @PageableDefault(size = 10, sort = "titulo") Pageable paginacion) {

        var page = topicoRepository.findByActivoTrue(paginacion)
                .map(DatosListadoTopico::new);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> obtenerTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ValidationException("No fue encontrado un tópico con el ID proporcionado"));

        if (!topico.getActivo()) {
            throw new ValidationException("No fue encontrado un tópico con el ID proporcionado");
        }

        return ResponseEntity.ok(new DatosRespuestaTopico(topico));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(@RequestBody @Valid DatosActualizarTopico datos) {
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