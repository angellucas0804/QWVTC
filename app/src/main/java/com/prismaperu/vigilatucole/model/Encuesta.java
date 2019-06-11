package com.prismaperu.vigilatucole.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Encuesta {

    @SerializedName("nombre")
    private String nombre;
    @SerializedName("preguntas")
    private List<Pregunta> preguntas;

    public Encuesta(String nombre, List<Pregunta> preguntas) {
        this.nombre = nombre;
        this.preguntas = preguntas;
    }

    public Encuesta() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }
}
