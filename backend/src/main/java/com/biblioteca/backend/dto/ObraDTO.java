package com.biblioteca.backend.dto;

import java.sql.Date;

public class ObraDTO {
    private String cod_barras;  // ID da obra
    private String titulo;
    private Date ano_lanc;

    // Getters e Setters
    public String getCod_barras() {
        return cod_barras;
    }



    public void setCod_barras(String cod_barras) {
        this.cod_barras = cod_barras;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getAno_lanc() {
        return ano_lanc;
    }

    public void setAno_lanc(Date ano_lanc) {
        this.ano_lanc = ano_lanc;
    }

    // Alternativamente, se quiser setar a data por String:
    /*
    public void setAno_lanc(String ano_lanc) {
        this.ano_lanc = Date.valueOf(ano_lanc);
    }
    */
}
