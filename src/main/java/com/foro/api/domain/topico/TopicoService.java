package com.foro.api.domain.topico;

import com.foro.api.domain.curso.Curso;
import com.foro.api.domain.curso.CursoRepository;
import com.foro.api.domain.usuario.Usuario;
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

    private Curso validarCursoActivo(Long cursoId) {
        return cursoRepository.findById(cursoId)
                .filter(Curso::getActivo)
                .orElseThrow(() -> new ValidationException("El curso no existe o no está activo"));
    }

    private Usuario validarUsuarioActivo(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .filter(Usuario::getActivo)
                .orElseThrow(() -> new ValidationException("El usuario no existe o no está activo"));
    }


    public DatosRespuestaTopico registrarTopico(DatosRegistroTopico datos) {
        var curso = validarCursoActivo(datos.cursoId());
        var usuario = validarUsuarioActivo(datos.autorId());

        var topico = new Topico(datos);
        topicoRepository.save(topico);

        return new DatosRespuestaTopico(topico);
    }

    public Topico obtenerTopicoPorId(Long id) {
        return topicoRepository.findById(id)
                .orElseThrow(() -> new ValidationException("No fue encontrado un tópico con el ID proporcionado"));
    }
}