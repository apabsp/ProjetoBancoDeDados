package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.AutorDTO;
import com.biblioteca.backend.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/autor")
public class AutorController{
    @Autowired private DatabaseService db;

    @PostMapping("/inserir")
    public String inserirAutor(@RequestBody AutorDTO dto) {
        String id = dto.getId() != null ? dto.getId() : UUID.randomUUID().toString();
        return db.inserirAutor(id, dto.getNome());
    }

    @PostMapping("/delete")
    public String deleteAutor(@RequestBody AutorDTO dto) {
        return db.deletarAutorPorNome(dto.getNome());
    }

    @GetMapping("/visualizar")
    public List<String> listarAutores() {
        //return db.listarAutores();  // still needs to implement
        System.out.println("Visualizar autores not implemented ytet");
        return List.of();
    }
}
