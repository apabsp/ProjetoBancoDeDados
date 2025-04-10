package com.biblioteca.backend.dto;

public class ObraDTO {
    private String titulo;
    private String ano;
    private String genero;
    //Cod de barras, em teoria, vai ser gerado depois.

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAno() {
        return ano;
    }
    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }
}
