package com.biblioteca.backend.service;

import org.springframework.stereotype.Service;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.List;

@Service
public class ObraService {

    private final String url = "jdbc:mysql://localhost:3306/biblioteca";
    private final String user = "root";
    private final String password = "12345";

    public void inserirObra(String codBarras, String titulo, String anoLanc, String genero) {
        String scriptPath = "src/main/resources/scripts/script-inserir-obra.sql";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            List<String> lines = Files.readAllLines(Paths.get(scriptPath), StandardCharsets.UTF_8);

            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("--")) continue;

                String query = line
                        .replace(":cod_barras", "'" + codBarras + "'")
                        .replace(":titulo", "'" + titulo + "'")
                        .replace(":ano_lanc", "'" + anoLanc + "'")
                        .replace(":genero", "'" + genero + "'");

                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(query);
                }
            }

            System.out.println("Obra inserida com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
