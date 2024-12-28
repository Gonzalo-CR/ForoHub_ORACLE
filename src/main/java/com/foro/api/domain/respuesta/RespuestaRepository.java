package com.foro.api.domain.respuesta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {
    @Query("""
            SELECT r FROM Respuesta r
            WHERE r.topico.id = :topicoId
            AND r.activo = true
            ORDER BY r.fechaCreacion DESC
            """)
    List<Respuesta> findByTopicoIdAndActivoTrue(Long topicoId);
}