package com.biblioteca.backend.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Service
public class DatabaseService {
    private final String url = "jdbc:mysql://localhost:3306/biblioteca";
    private final String user = "root";
    private final String password = "12345";
    //This should read the script line by line
    public void executarScript(String scriptPath) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(
                             getClass().getResourceAsStream(scriptPath)))) {

            StringBuilder sql = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                sql.append(linha).append("\n");
                if (linha.trim().endsWith(";")) {
                    stmt.execute(sql.toString());
                    sql.setLength(0);
                }
            }

            System.out.println("Script executado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
