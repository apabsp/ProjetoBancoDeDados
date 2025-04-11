package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.ObraDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import com.biblioteca.backend.service.DatabaseService;
import com.biblioteca.backend.dto.ObraDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/obra")
public class ObraController {

    @Autowired
    private DatabaseService databaseService;

    @PostMapping("/inserir")
    public String inserirObra(@RequestBody ObraDTO obraDTO) {
        // ano is already converted to Date in ObraDTO, just pass it along
        return databaseService.inserirObra(obraDTO.getTitulo(), obraDTO.getAno(), obraDTO.getGenero());
    }

    @DeleteMapping("/deletarPorTitulo")
    public String deletarObraPorTitulo(@RequestParam String titulo) {
        return databaseService.deletarObraPorTitulo(titulo);
    }

    @DeleteMapping("/deletar/{id}")
    public String deletarObra(@PathVariable Long id) {
        return databaseService.deletarObra(id);
    }

    @PutMapping("/atualizar/{id}")
    public String atualizarObra(@PathVariable Long id, @RequestBody ObraDTO obraDTO) {
        return databaseService.atualizarObra(id, obraDTO.getTitulo(), obraDTO.getAno(), obraDTO.getGenero());
    }

    @GetMapping("/visualizar")
    public String visualizarObras() {
        return databaseService.visualizarObras();
    }
}


/*
@RestController
public class ObraController {

    private final String url = "jdbc:mysql://localhost:3306/biblioteca";
    private final String user = "root";
    private final String password = "12345";

    @PostMapping("/api/obra/inserir")
    public ResponseEntity<String> inserirObra(@RequestBody ObraDTO obra) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String script = Files.readString(Paths.get("src/main/resources/scripts/script-inserir-obra.sql"));

            script = script.replace("${titulo}", obra.getTitulo())
                    .replace("${ano}", obra.getAno())
                    .replace("${genero}", obra.getGenero());

            Statement stmt = conn.createStatement();
            for (String linha : script.split(";")) {
                if (!linha.trim().isEmpty()) {
                    stmt.executeUpdate(linha.trim());
                }
            }

            return ResponseEntity.ok("Obra inserida com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao inserir obra: " + e.getMessage());
        }
    }
}
*/

