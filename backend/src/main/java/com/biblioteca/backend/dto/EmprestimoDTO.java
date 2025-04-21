package com.biblioteca.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmprestimoDTO {
    private String       id;
    private String       hora;               // "HH:mm:ss"
    private LocalDateTime dataPrevistaDev;   // datetime
    private LocalDate dataDevolucao;      // date
    private LocalDate    dataEmprestimo;     // date
    private String       fkExemplar;
    private String       fkCliente;


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

    public LocalDateTime getDataPrevistaDev() {
        return dataPrevistaDev;
    }

    public void setDataPrevistaDev(LocalDateTime dataPrevistaDev) {
        this.dataPrevistaDev = dataPrevistaDev;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
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
