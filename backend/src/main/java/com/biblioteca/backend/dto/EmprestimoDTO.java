package com.biblioteca.backend.controller;

public class EmprestimoDTO {
    private String id;
    private String hora;               // “HH:mm:ss”
    private String dataPrevistaDev;    // “HH:mm:ss” or ISO date?
    private String dataDevolucao;      // “HH:mm:ss”
    private String dataEmprestimo;     // “HH:mm:ss” thiscouldbeit
    private String fkExemplar;
    private String fkCliente;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDataPrevistaDev() {
        return dataPrevistaDev;
    }

    public void setDataPrevistaDev(String dataPrevistaDev) {
        this.dataPrevistaDev = dataPrevistaDev;
    }

    public String getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(String dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public String getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(String dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public String getFkExemplar() {
        return fkExemplar;
    }

    public void setFkExemplar(String fkExemplar) {
        this.fkExemplar = fkExemplar;
    }

    public String getFkCliente() {
        return fkCliente;
    }

    public void setFkCliente(String fkCliente) {
        this.fkCliente = fkCliente;
    }
}
