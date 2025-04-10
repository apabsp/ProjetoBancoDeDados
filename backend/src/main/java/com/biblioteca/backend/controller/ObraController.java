package com.biblioteca.backend.controller;

import com.biblioteca.backend.dto.ObraDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.nio.file.*;
import java.sql.*;
import java.util.*;

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
