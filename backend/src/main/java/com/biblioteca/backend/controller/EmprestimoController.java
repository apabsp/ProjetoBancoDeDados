package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.EmprestimoDTO;
import com.biblioteca.backend.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/api/emprestimo")
public class EmprestimoController {
    @Autowired private DatabaseService db;

    @PostMapping("/inserir")
    public String inserirEmprestimo(@RequestBody EmprestimoDTO dto) {

            java.sql.Time      hora            = java.sql.Time.valueOf(dto.getHora());
            java.sql.Timestamp previstaDev     = java.sql.Timestamp.valueOf(dto.getDataPrevistaDev());
            java.sql.Date      dataDevolucao   = java.sql.Date.valueOf(dto.getDataDevolucao());
            java.sql.Date      dataEmprestimo  = java.sql.Date.valueOf(dto.getDataEmprestimo());

            return db.inserirEmprestimoAluga(
                    dto.getId(), hora, previstaDev, dataDevolucao, dataEmprestimo,
                    dto.getFkExemplar(), dto.getFkCliente()
            );
    }

    @GetMapping("/visualizar")
    public List<String> visualizarEmprestimos() {
        return db.listarEmprestimos();
    }

    @DeleteMapping("/delete")
    public String deleteEmprestimo(@RequestParam String id) {
        return db.deletarEmprestimo(id);
    }

    @PutMapping("/alterar")
    public String alterarEmprestimo(@RequestBody EmprestimoDTO emprestimoDTO) {
        // Assuming `fkFuncionario` is part of the DTO and current timestamp will be used as the timestamp.

        // Convert LocalDateTime to java.sql.Date for data_prevista_dev and dataEmprestimo
        java.sql.Date dataPrevistaDev = java.sql.Date.valueOf(emprestimoDTO.getDataPrevistaDev().toLocalDate());
        java.sql.Date dataDevolucao = java.sql.Date.valueOf(emprestimoDTO.getDataDevolucao());
        java.sql.Date dataEmprestimo = java.sql.Date.valueOf(emprestimoDTO.getDataEmprestimo());

        return db.alterarEmprestimo(
                emprestimoDTO.getId(),
                emprestimoDTO.getHora(),
                dataPrevistaDev,         // data_prevista_dev
                dataDevolucao,           // data_devolucao
                dataEmprestimo,          // data_emprestimo
                emprestimoDTO.getFkExemplar(),
                emprestimoDTO.getFkCliente(),
                emprestimoDTO.getFkFuncionario()
        );
    }

}

