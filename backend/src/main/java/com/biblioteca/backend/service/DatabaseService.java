package com.biblioteca.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.UUID;

@Service
public class DatabaseService {

    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";


    // Method to execute SQL script (like schema.sql)
    public void executarScript(String scriptPath) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(getClass().getResourceAsStream(scriptPath)))) {

            StringBuilder sql = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                sql.append(linha).append("\n");
                // Execute each SQL statement when it ends with a semicolon
                if (linha.trim().endsWith(";")) {
                    stmt.executeUpdate(sql.toString());
                    sql.setLength(0);  // Clear the StringBuilder for the next SQL statement
                }
            }
            System.out.println("Script executado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao executar o script: " + e.getMessage());
        }
    }

    public String inserirEditora(String id, String nome) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO Editora (id, nome) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, id);
                stmt.setString(2, nome);
                stmt.executeUpdate();
                return "Editora inserida com sucesso!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir editora: " + e.getMessage();
        }
    }

    public String inserirAutor(String id, String nome) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO Autor (id, nome) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, id);
                stmt.setString(2, nome);
                stmt.executeUpdate();
                return "Autor inserido com sucesso!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir autor: " + e.getMessage();
        }
    }


    // Method to insert a new "Obra"
    public String inserirObra(String titulo, java.sql.Date ano, String genero) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Generate a random UUID for cod_barras
            String codBarras = UUID.randomUUID().toString();  // Generate a unique string for cod_barras

            // Correct SQL query to insert values into the correct columns
            String sql = "INSERT INTO Obra (cod_barras, titulo, ano_lanc) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, codBarras);  // Use the generated UUID for cod_barras
                stmt.setString(2, titulo);     // Correct column for titulo
                stmt.setDate(3, ano);           // Correct column for ano_lanc (Date)

                stmt.executeUpdate();
                return "Obra inserida com sucesso!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir obra: " + e.getMessage();
        }
    }
    // Still need to work on this one
    public String deletarObraPorTitulo(String titulo) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "DELETE FROM obra WHERE titulo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, titulo);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Obra deletada com sucesso!";
            } else {
                return "Nenhuma obra encontrada com esse título.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao deletar obra: " + e.getMessage();
        }
    }

    // Method to delete an "Obra" with an id
    public String deletarObra(Long id) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // The SQL query to delete the Obra by its ID (assuming `id` is the `cod_barras` in the DB)
            String sql = "DELETE FROM Obra WHERE cod_barras = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setLong(1, id);  // Set the id as the parameter for the delete query
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    return "Obra deletada com sucesso!";
                } else {
                    return "Obra não encontrada!";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar obra: " + e.getMessage();
        }
    }

    // Method to update an "Obra"
    public String atualizarObra(Long id, String titulo, java.sql.Date ano, String genero) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "UPDATE Obra SET titulo = ?, ano_lanc = ?, genero = ? WHERE cod_barras = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, titulo);  // Set the title
                stmt.setDate(2, ano);        // Set the date
                stmt.setString(3, genero);  // Set the genre
                stmt.setLong(4, id);        // Set the id for the WHERE clause

                stmt.executeUpdate();
                return "Obra atualizada com sucesso!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao atualizar obra: " + e.getMessage();
        }
    }

    // Method to select all "Obras"
    public String visualizarObras() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM Obra";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                var resultSet = stmt.executeQuery();

                StringBuilder result = new StringBuilder();
                while (resultSet.next()) {
                    result.append("Cod Barras: ").append(resultSet.getString("cod_barras"))
                            .append(", Titulo: ").append(resultSet.getString("titulo"))
                            .append(", Ano: ").append(resultSet.getDate("ano_lanc"))
                            .append("\n");
                }
                return result.toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao visualizar obras: " + e.getMessage();
        }
    }

    public String vincularObraAEditora(String codBarras, String idEditora) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO Publica (fk_Obra_cod_barras, fk_Editora_id) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, codBarras);
                stmt.setString(2, idEditora);
                stmt.executeUpdate();
                return "Obra vinculada à editora com sucesso!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao vincular obra à editora: " + e.getMessage();
        }
    }

    public String vincularAutorAObra(String idAutor, String codBarras) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO Escreve (fk_Autor_id, fk_Obra_cod_barras) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, idAutor);
                stmt.setString(2, codBarras);
                stmt.executeUpdate();
                return "Autor vinculado à obra com sucesso!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao vincular autor à obra: " + e.getMessage();
        }
    }

    public String vincularGeneroAObra(String nomeGenero, String codBarras) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO Pertence (fk_Genero_nome, fk_Obra_cod_barras) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, nomeGenero);
                stmt.setString(2, codBarras);
                stmt.executeUpdate();
                return "Gênero vinculado à obra com sucesso!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao vincular gênero à obra: " + e.getMessage();
        }
    }

    public String visualizarObrasPorEditora(String idEditora) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT O.titulo, O.ano_lanc " +
                    "FROM Obra O " +
                    "JOIN Publica P ON O.cod_barras = P.fk_Obra_cod_barras " +
                    "WHERE P.fk_Editora_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, idEditora);
                ResultSet resultSet = stmt.executeQuery();

                StringBuilder result = new StringBuilder();
                while (resultSet.next()) {
                    result.append("Título: ").append(resultSet.getString("titulo"))
                            .append(", Ano de Lançamento: ").append(resultSet.getDate("ano_lanc"))
                            .append("\n");
                }
                return result.toString();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao visualizar obras por editora: " + e.getMessage();
        }
    }

    /*
    public String visualizarAutoresPorObra(String codBarras) {
    try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
        String sql = "SELECT A.nome " +
                     "FROM Autor A " +
                     "JOIN Escreve E ON A.id = E.fk_Autor_id " +
                     "WHERE E.fk_Obra_cod_barras = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codBarras);
            ResultSet resultSet = stmt.executeQuery();

            StringBuilder result = new StringBuilder();
            while (resultSet.next()) {
                result.append("Autor: ").append(resultSet.getString("nome")).append("\n");
            }
            return result.toString();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return "Erro ao visualizar autores por obra: " + e.getMessage();
    }
}

     */

}
