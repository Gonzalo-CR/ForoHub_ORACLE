package com.foro.api.domain.topico;

import com.foro.api.domain.curso.CursoRepository;
import com.foro.api.domain.usuario.UsuarioRepository;
import com.foro.api.infra.errores.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public DatosRespuestaTopico registrarTopico(DatosRegistroTopico datos) {
        // Validar que existan el curso y el usuario
        var curso = cursoRepository.findById(datos.cursoId())
                .orElseThrow(() -> new ValidationException("No existe un curso con el ID proporcionado"));

        var usuario = usuarioRepository.findById(datos.autorId())
                .orElseThrow(() -> new ValidationException("No existe un usuario con el ID proporcionado"));

        if (!curso.getActivo() || !usuario.getActivo()) {
            throw new ValidationException("El curso o usuario no están activos");
        }

        var topico = new Topico(datos);
        topicoRepository.save(topico);

        return new DatosRespuestaTopico(topico);
    }

    public Topico obtenerTopicoPorId(Long id) {
        return topicoRepository.findById(id)
                .orElseThrow(() -> new ValidationException("No fue encontrado un tópico con el ID proporcionado"));
    }
}