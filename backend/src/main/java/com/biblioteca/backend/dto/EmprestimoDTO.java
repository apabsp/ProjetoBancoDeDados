package com.biblioteca.backend.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmprestimoDTO {
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataPrevistaDev;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataDevolucao;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dataEmprestimo;
    private String fkExemplar;
    private String fkCliente;
    private String fkFuncionario;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDateTime getDataPrevistaDev() { return dataPrevistaDev; }
    public void setDataPrevistaDev(LocalDateTime dataPrevistaDev) {
        this.dataPrevistaDev = dataPrevistaDev;
    }

    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public LocalDateTime getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDateTime dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public String getFkExemplar() { return fkExemplar; }
    public void setFkExemplar(String fkExemplar) {
        this.fkExemplar = fkExemplar;
    }

    public String getFkCliente() { return fkCliente; }
    public void setFkCliente(String fkCliente) {
        this.fkCliente = fkCliente;
    }

    public String getFkFuncionario() { return fkFuncionario; }
    public void setFkFuncionario(String fkFuncionario) {
        this.fkFuncionario = fkFuncionario;
    }
}