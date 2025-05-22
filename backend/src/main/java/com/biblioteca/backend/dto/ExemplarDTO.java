package com.biblioteca.backend.dto;

public class ExemplarDTO {
    private String id;
    private String fkEdicao;
    private String fkArtigo;
    private String fkEstantePrateleira;
    private String fkEstanteNumero;

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFkEdicao() {
        return fkEdicao;
    }

    public void setFkEdicao(String fkEdicao) {
        this.fkEdicao = fkEdicao;
    }

    public String getFkArtigo() {
        return fkArtigo;
    }

    public void setFkArtigo(String fkArtigo) {
        this.fkArtigo = fkArtigo;
    }

    public String getFkEstantePrateleira() {
        return fkEstantePrateleira;
    }

    public void setFkEstantePrateleira(String fkEstantePrateleira) {
        this.fkEstantePrateleira = fkEstantePrateleira;
    }

    public String getFkEstanteNumero() {
        return fkEstanteNumero;
    }

    public void setFkEstanteNumero(String fkEstanteNumero) {
        this.fkEstanteNumero = fkEstanteNumero;
    }
}
