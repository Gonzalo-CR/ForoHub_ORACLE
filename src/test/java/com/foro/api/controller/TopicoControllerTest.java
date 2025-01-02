package com.foro.api.controller;

import com.foro.api.domain.topico.TopicoRepository;
import com.foro.api.domain.topico.StatusTopico;
import com.foro.api.domain.topico.*;
import com.foro.api.domain.topico.DatosActualizarTopico;
import com.foro.api.domain.topico.DatosRegistroTopico;
import com.foro.api.domain.topico.DatosListadoTopico;
import com.foro.api.domain.topico.DatosRespuestaTopico;

import com.foro.api.infra.errores.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TopicoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<DatosRegistroTopico> datosRegistroTopicoJson;

    @Autowired
    private JacksonTester<DatosRespuestaTopico> datosRespuestaTopicoJson;

    @Autowired
    private JacksonTester<DatosListadoTopico> datosListadoTopicoJson;

    @Mock
    private TopicoRepository topicoRepository;

    @InjectMocks
    private TopicoController topicoController;
    private Object DatosActualizarTopico;
    private Object DatosRegistroTopico;

    @Test
    @DisplayName("Debería devolver http 400 cuando la request no tenga datos")
    void registrarTopico_escenario1() throws Exception {
        var response = mvc.perform(post("/topicos"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Debería devolver http 201 cuando la request reciba un JSON válido")
    void registrarTopico_escenario2() throws Exception {
        var datos = new DatosRegistroTopico("Título de prueba", "Mensaje de prueba", 1L, 1L);

        when(topicoRepository.save(any(Topico.class))).thenReturn(new Topico(datos));

        var response = mvc.perform(post("/topicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(datosRegistroTopicoJson.write(datos).getJson())
                )
                .andReturn().getResponse();

        var jsonEsperado = datosRespuestaTopicoJson.write(
                new DatosRespuestaTopico(new Topico(datos))
        ).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("Debería devolver http 200 cuando se obtienen los tópicos")
    void listadoTopicos() throws Exception {
        // Crear datos ficticios para el test
        var topicos = Page.empty();

        when(topicoRepository.findByActivoTrue(any(Pageable.class))).thenReturn(topicoRepository.findByActivoTrue(topicos.getPageable()));

        var response = mvc.perform(get("/topicos"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("Debería devolver http 404 cuando no se encuentra un tópico por ID")
    void obtenerTopico_escenario1() throws Exception {
        when(topicoRepository.findById(any())).thenThrow(new ValidationException("No fue encontrado un tópico con el ID proporcionado"));

        var response = mvc.perform(get("/topicos/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Debería devolver http 200 cuando se obtiene un tópico por ID")
    void obtenerTopico_escenario2() throws Exception {
        var topico = new Topico((com.foro.api.domain.topico.DatosRegistroTopico) DatosRegistroTopico);
        when(topicoRepository.findById(any())).thenReturn(java.util.Optional.of(topico));

        var response = mvc.perform(get("/topicos/1"))
                .andReturn().getResponse();

        var jsonEsperado = datosRespuestaTopicoJson.write(new DatosRespuestaTopico(topico)).getJson();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

//    @Test
//    @DisplayName("Debería devolver http 404 cuando se intenta actualizar un tópico no existente")
//    void actualizarTopico_escenario1() throws Exception {
//        var datos = new DatosActualizarTopico(1L,
//                "Nuevo título",
//                "Nuevo mensaje",
//                new StatusTopico());
//
//        when(topicoRepository.findById(any())).thenThrow(new ValidationException("No fue encontrado un tópico con el ID proporcionado"));
//
//        var response = mvc.perform(put("/topicos")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(datosRegistroTopicoJson.write((com.foro.api.domain.topico.DatosRegistroTopico) DatosRegistroTopico).getJson())
//                )
//                .andReturn().getResponse();
//
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
//    }
//
//    @Test
//    @DisplayName("Debería devolver http 200 cuando un tópico es actualizado exitosamente")
//    void actualizarTopico_escenario2() throws Exception {
//        var datos = new DatosActualizarTopico(1L, "Nuevo título", "Nuevo mensaje", new StatusTopico());
//        var topico = new Topico();
//
//        when(topicoRepository.findById(any())).thenReturn(Optional.of(topico));
//        when(topicoRepository.save(any(Topico.class))).thenReturn(topico);
//
//        var response = mvc.perform(put("/topicos")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(datosRegistroTopicoJson.write().getJson())
//                )
//                .andReturn().getResponse();
//
//        var jsonEsperado = datosRespuestaTopicoJson.write(new DatosRespuestaTopico(topico)).getJson();
//
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
//    }

    @Test
    @DisplayName("Debería devolver http 204 cuando un tópico es eliminado exitosamente")
    void eliminarTopico() throws Exception {
        var topico = new Topico((com.foro.api.domain.topico.DatosRegistroTopico) DatosRegistroTopico);

        when(topicoRepository.findById(any())).thenReturn(java.util.Optional.of(topico));

        var response = mvc.perform(delete("/topicos/1"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
