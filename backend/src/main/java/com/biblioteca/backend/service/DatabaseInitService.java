// src/main/java/com/biblioteca/backend/service/DatabaseInitService.java

package com.biblioteca.backend.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Service
public class DatabaseInitService {

    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "12345"; // ajuste sua senha

    public void recriarTabelas() {
        try {
            // Lê o conteúdo do script.sql
            var resource = new ClassPathResource("script.sql");
            String sql = Files.readString(resource.getFile().toPath());

            // Executa o script
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                for (String comando : sql.split(";")) {
                    if (!comando.trim().isEmpty()) {
                        stmt.execute(comando);
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao recriar tabelas: " + e.getMessage(), e);
        }
    }
}
