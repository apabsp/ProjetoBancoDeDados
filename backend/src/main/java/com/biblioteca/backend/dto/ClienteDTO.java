package com.biblioteca.backend.dto;

public class ClienteDTO {
    private String id;
    private String historico;
    private String fkPessoaId;
    private String nomePessoa; // Pra que eu possa importar do nome da pessoa


    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }


    public String getFkPessoaId() {
        return fkPessoaId;
    }

    public void setFkPessoaId(String fkPessoaId) {
        this.fkPessoaId = fkPessoaId;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
