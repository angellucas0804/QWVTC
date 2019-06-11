package com.prismaperu.vigilatucole.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pregunta {

    @SerializedName("texto")
    private String texto;
    @SerializedName("orden")
    private int orden;
    @SerializedName("portada")
    private String portada;
    @SerializedName("respuestas")
    private List<Respuesta> respuestas;

    public Pregunta(String texto, int orden, String portada, List<Respuesta> respuestas) {
        this.texto = texto;
        this.orden = orden;
        this.portada = portada;
        this.respuestas = respuestas;
    }

    public Pregunta() {

    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
}
