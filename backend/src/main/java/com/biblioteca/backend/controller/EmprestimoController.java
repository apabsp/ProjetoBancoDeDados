package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.EmprestimoDTO;
import com.biblioteca.backend.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID; // Randomizer

@RestController
@RequestMapping("/api/emprestimo")
public class EmprestimoController {

    @Autowired
    private DatabaseService db;

    @GetMapping("/dias-ate-devolucao")
    public String getDiasAteDevolucao(@RequestParam String idEmprestimo) {
        try {
            Integer dias = db.calcularDiasAteDevolucao(idEmprestimo);
            return dias != null ? dias.toString() : "Empréstimo não encontrado";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao calcular dias até devolução: " + e.getMessage();
        }
    }


    // Endpoint para inserir um novo empréstimo
    @PostMapping("/inserir")
    public String inserirEmprestimo(@RequestBody EmprestimoDTO dto) {
        try {
            String id = "emp-" + UUID.randomUUID();

            Timestamp dataEmprestimo = dto.getDataEmprestimo() != null
                    ? Timestamp.valueOf(dto.getDataEmprestimo())
                    : null;

            Timestamp dataPrevistaDev = dto.getDataPrevistaDev() != null
                    ? Timestamp.valueOf(dto.getDataPrevistaDev())
                    : null;

            return db.inserirEmprestimoAluga(
                    dataPrevistaDev,
                    null, // quero que dataDevolucao seja null por padrão. Não faz sentido inserir com a devolução já feita. Deixar isso para o alterar
                    dataEmprestimo,
                    dto.getFkExemplar(),
                    dto.getFkCliente(),
                    dto.getFkFuncionario()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao inserir empréstimo: " + e.getMessage();
        }
    }

    @GetMapping("/visualizar/{id}")
    public EmprestimoDTO visualizarEmprestimoPorId(@PathVariable String id) {
        return db.buscarEmprestimoPorId(id);
    }




    @GetMapping("/visualizar")
    public List<EmprestimoDTO> visualizarEmprestimos() {
        return db.listarEmprestimosDTO();
    }

    //
    /*@GetMapping("/buscar")
    public EmprestimoDTO buscarEmprestimo(@RequestParam String id) {
        return db.buscarEmprestimoPorId(id);
    }*/


    @DeleteMapping("/deletar")
    public String deletarEmprestimo(@RequestParam String id) {
        return db.deletarEmprestimo(id);
    }
    //Atualizar
    @PutMapping("/alterar")
    public String alterarEmprestimo(@RequestParam String id, @RequestBody EmprestimoDTO dto) {
        try {
            // Conversão dos tipos de data
            Timestamp dataPrevistaDev = dto.getDataPrevistaDev() != null ?
                    Timestamp.valueOf(dto.getDataPrevistaDev()) : null;

            Date dataDevolucao = dto.getDataDevolucao() != null ?
                    Date.valueOf(dto.getDataDevolucao()) : null;

            Timestamp dataEmprestimo = dto.getDataEmprestimo() != null ?
                    Timestamp.valueOf(dto.getDataEmprestimo()) : null;

            return db.alterarEmprestimo(
                    id,
                    dataPrevistaDev,
                    dataDevolucao,
                    dataEmprestimo,
                    dto.getFkExemplar(),
                    dto.getFkCliente(),
                    dto.getFkFuncionario()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao atualizar empréstimo: " + e.getMessage();
        }
    }

    // Endpoint para listar empréstimos por cliente?
}