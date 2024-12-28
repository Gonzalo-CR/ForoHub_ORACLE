package com.foro.api.service;


import com.foro.api.domain.respuesta.Respuesta;
import com.foro.api.domain.respuesta.DatosRegistroRespuesta;
import com.foro.api.domain.usuario.UsuarioRepository;
import com.foro.api.domain.topico.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RespuestaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    public Respuesta crearRespuesta(DatosRegistroRespuesta datos) {
        var usuario = usuarioRepository.findById(datos.autorId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        var topico = topicoRepository.findById(datos.topicoId())
                .orElseThrow(() -> new RuntimeException("Tópico no encontrado"));

        return new Respuesta(datos, usuario, topico);
    }
}