package com.biblioteca.backend.dto;

import java.sql.Date;

public class ObraDTO {
    private String titulo;
    private Date ano;  // Keep as java.sql.Date to store the full date
    private String genero;

    // Getters and Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getAno() {
        return ano;
    }

    // Setter: Convert the String to Date
    public void setAno(String ano) {
        this.ano = Date.valueOf(ano);  // Convert from String (e.g., "2000-06-16") to java.sql.Date
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
