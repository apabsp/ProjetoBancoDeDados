package com.biblioteca.backend.dto;

public class ObraDTO {
    private String codBarras;
    private String titulo;
    private String anoLanc;
    private String genero;

    public ObraDTO() {
    }

    public ObraDTO(String codBarras, String titulo, String anoLanc, String genero) {
        this.codBarras = codBarras;
        this.titulo = titulo;
        this.anoLanc = anoLanc;
        this.genero = genero;
    }

    public String getCodBarras() {
        return codBarras;
    }

    public void setCodBarras(String codBarras) {
        this.codBarras = codBarras;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAnoLanc() {
        return anoLanc;
    }

    public void setAnoLanc(String anoLanc) {
        this.anoLanc = anoLanc;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
