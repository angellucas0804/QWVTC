package com.prismaperu.vigilatucole.model;

import com.google.gson.annotations.SerializedName;

public class Respuesta {

    @SerializedName("texto")
    private String texto;
    @SerializedName("valor")
    private int valor;
    @SerializedName("imagen")
    private String imagen;

    public Respuesta(String texto, int valor, String imagen) {
        this.texto = texto;
        this.valor = valor;
        this.imagen = imagen;
    }

    public Respuesta() {

    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
