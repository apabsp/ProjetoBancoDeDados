package com.biblioteca.backend;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.sql.*;

@Component
public class DatabaseInitializer {

    private final String url = "jdbc:mysql://localhost:3306/biblioteca";
    private final String user = "root";
    private final String password = "12345";

    @PostConstruct // Este método será chamado automaticamente após o Spring iniciar o contexto
    public void init() {
        /*
        System.out.println("Iniciando criação das tabelas...");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected!");

            Statement stmt = conn.createStatement();

            // Tabelas independentes
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Pessoa (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "nome VARCHAR(100), " +
                    "complemento VARCHAR(100), " +
                    "cep VARCHAR(50))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Editora (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Autor (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Genero (" +
                    "nome VARCHAR(50) PRIMARY KEY)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Estante (" +
                    "prateleira VARCHAR(50), " +
                    "numero VARCHAR(50), " +
                    "PRIMARY KEY (prateleira, numero))");

            // Dependentes
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Obra (" +
                    "cod_barras VARCHAR(50) PRIMARY KEY, " +
                    "titulo VARCHAR(100) NOT NULL, " +
                    "ano_lanc DATE)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Cliente (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "historico VARCHAR(100), " +
                    "fk_Pessoa_id VARCHAR(50), " +
                    "FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa(id))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Funcionario (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "fk_Pessoa_id VARCHAR(50), " +
                    "FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa(id))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Livro (" +
                    "fk_Obra_cod_barras VARCHAR(50), " +
                    "PRIMARY KEY (fk_Obra_cod_barras), " +
                    "FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Artigo (" +
                    "fk_Obra_cod_barras VARCHAR(50), " +
                    "titulo VARCHAR(100), " +
                    "PRIMARY KEY (fk_Obra_cod_barras), " +
                    "FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Emprestimo_aluga (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "hora TIME, " +
                    "data_prevista_dev DATE, " +
                    "data_devolucao DATE, " +
                    "fk_Cliente_id VARCHAR(50), " +
                    "FOREIGN KEY (fk_Cliente_id) REFERENCES Cliente(id))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Telefone (" +
                    "telefone_pk VARCHAR(50) PRIMARY KEY, " +
                    "numero INT, " +
                    "fk_Pessoa_id VARCHAR(50), " +
                    "FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa(id))");

            // Tabelas de junção
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS publica (" +
                    "fk_Obra_cod_barras VARCHAR(50), " +
                    "fk_Editora_id VARCHAR(50), " +
                    "PRIMARY KEY (fk_Obra_cod_barras, fk_Editora_id), " +
                    "FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras), " +
                    "FOREIGN KEY (fk_Editora_id) REFERENCES Editora(id))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS escreve (" +
                    "fk_Autor_id VARCHAR(50), " +
                    "fk_Obra_cod_barras VARCHAR(50), " +
                    "PRIMARY KEY (fk_Autor_id, fk_Obra_cod_barras), " +
                    "FOREIGN KEY (fk_Autor_id) REFERENCES Autor(id), " +
                    "FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Pertence (" +
                    "fk_Genero_nome VARCHAR(50), " +
                    "fk_Obra_cod_barras VARCHAR(50), " +
                    "PRIMARY KEY (fk_Genero_nome, fk_Obra_cod_barras), " +
                    "FOREIGN KEY (fk_Genero_nome) REFERENCES Genero(nome), " +
                    "FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras))");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Edicao (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "livro_cod_barras VARCHAR(50), " +
                    "numero INT, " +
                    "FOREIGN KEY (livro_cod_barras) REFERENCES Livro(fk_Obra_cod_barras))");

            // Consulta de teste
            ResultSet result = stmt.executeQuery("SELECT * FROM Obra");
            System.out.println("Conteúdo da tabela Obra:");
            while (result.next()) {
                System.out.println("Cod Barras: " + result.getString("cod_barras") +
                        ", Título: " + result.getString("titulo"));
            }

            System.out.println("Tabelas criadas com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao criar as tabelas:");
            e.printStackTrace();
        }
        */
    }
}