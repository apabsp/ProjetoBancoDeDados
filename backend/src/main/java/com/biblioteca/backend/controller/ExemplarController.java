package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.ExemplarDTO;
import com.biblioteca.backend.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exemplar")
public class ExemplarController {

    @Autowired
    private DatabaseService db;

    @PostMapping("/inserir")
    public String inserirExemplar(@RequestBody ExemplarDTO dto) {
        try {
            String id = "ex-" + UUID.randomUUID(); // Geração de ID aqui
            return db.inserirExemplar(id, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao inserir exemplar: " + e.getMessage();
        }
    }

    @GetMapping("/visualizar")
    public List<ExemplarDTO> listarExemplares() {
        return db.listarExemplares();
    }

    @GetMapping("/visualizar/{id}")
    public ExemplarDTO buscarExemplarPorId(@PathVariable String id) {
        return db.buscarExemplarPorId(id);
    }

    @PutMapping("/alterar")
    public String alterarExemplar(@RequestParam String id, @RequestBody ExemplarDTO dto) {
        try {
            return db.alterarExemplar(id, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao atualizar exemplar: " + e.getMessage();
        }
    }

    @DeleteMapping("/deletar")
    public String deletarExemplar(@RequestParam String id) {
        return db.deletarExemplar(id);
    }
}
