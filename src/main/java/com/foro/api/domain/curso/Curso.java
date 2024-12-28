package com.foro.api.domain.curso;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "cursos")
@Entity(name = "Curso")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String categoria;
    private Boolean activo = true;

    public Curso(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Boolean getActivo() {
        return activo; // Devuelve el estado de actividad del curso
    }

    // Setter para el nombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Setter para el estado de actividad
    public void setActivo(Boolean activo) {
        this.activo = activo; // Permite modificar el estado de actividad del curso
    }

}