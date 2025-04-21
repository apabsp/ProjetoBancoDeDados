package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.GeneroDTO;
import com.biblioteca.backend.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/genero")
public class GeneroController {
    @Autowired
    private DatabaseService db;

    @PostMapping("/inserir")
    public String inserirGenero(@RequestBody GeneroDTO dto) {
        return db.inserirGenero(dto.getNome());
    }

    @GetMapping("/visualizar")
    public List<String> listarGeneros() {
        //
        // return db.listarGeneros();  // Still need to implement this
        System.out.println("listar generos not implemented yet");
        return Collections.emptyList();
    }
}
