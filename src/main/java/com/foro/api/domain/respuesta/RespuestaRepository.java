package com.foro.api.domain.respuesta;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    List<Respuesta> findByTopicoIdAndActivoTrue(Long topicoId);
}