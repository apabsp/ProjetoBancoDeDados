package com.biblioteca.backend.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public List<String> listarEditoras(){
        List<String> nomes = new ArrayList<>();

        String sql = "SELECT nome FROM Editora";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                nomes.add(rs.getString("nome"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("erro no listareditoras");
        }

        return nomes;
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

    public String deletarAutorPorNome(String nome){
        String sql = "DELETE FROM Autor WHERE nome = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return "Autor removidocom sucesso!";
            } else {
                return "Nenhuma autor encontrado com nome: " + nome;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar autor: " + e.getMessage();
        }
    }



    public String inserirGenero(String nome) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO Genero (nome) VALUES (?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, nome);
                stmt.executeUpdate();
                return "Gênero inserido com sucesso!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir gênero: " + e.getMessage();
        }
    }

    // Method to insert a new "Obra"
    public String inserirObra(String titulo, java.sql.Date ano, String genero) {
            try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
                String codBarras = UUID.randomUUID().toString();

                // 1) se veio um gênero, garanta que ele exista em Genero
                if (genero != null && !genero.trim().isEmpty()) {
                    String sqlFind = "SELECT nome FROM Genero WHERE nome = ?";
                    try (PreparedStatement psFind = connection.prepareStatement(sqlFind)) {
                        psFind.setString(1, genero.trim());
                        try (ResultSet rs = psFind.executeQuery()) {
                            if (!rs.next()) {
                                String sqlInsertGen = "INSERT INTO Genero(nome) VALUES(?)";
                                try (PreparedStatement psInsertGen = connection.prepareStatement(sqlInsertGen)) {
                                    psInsertGen.setString(1, genero.trim());
                                    psInsertGen.executeUpdate();
                                }
                            }
                        }
                    }
                }

                // 2) monta o INSERT em Obra incluindo o campo genero somente se tiver vindo
                String baseSql =
                        "INSERT INTO Obra (cod_barras, titulo, ano_lanc" +
                                (genero != null && !genero.trim().isEmpty() ? ", genero" : "") +
                                ") VALUES (?, ?, ?" +
                                (genero != null && !genero.trim().isEmpty() ? ", ?" : "") +
                                ")";
                try (PreparedStatement stmt = connection.prepareStatement(baseSql)) {
                    int idx = 1;
                    stmt.setString(idx++, codBarras);
                    stmt.setString(idx++, titulo);
                    stmt.setDate(idx++, ano);

                    if (genero != null && !genero.trim().isEmpty()) {
                        stmt.setString(idx, genero.trim());
                    }

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

    public String visualizarObrasPorAutor(String idAutor) {
        String sql = """
        SELECT O.cod_barras,
               O.titulo,
               O.ano_lanc
          FROM Obra O
          JOIN Escreve E
            ON O.cod_barras = E.fk_Obra_cod_barras
         WHERE E.fk_Autor_id = ?
        """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, idAutor);
            ResultSet rs = stmt.executeQuery();

            StringBuilder resultado = new StringBuilder();
            while (rs.next()) {
                resultado.append("Cod Barras: ")
                        .append(rs.getString("cod_barras"))
                        .append(", Título: ")
                        .append(rs.getString("titulo"))
                        .append(", Ano: ")
                        .append(rs.getDate("ano_lanc"))
                        .append("\n");
            }

            if (resultado.length() == 0) {
                return "Nenhuma obra encontrada para o autor de ID " + idAutor;
            }
            return resultado.toString();

        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao visualizar obras por autor: " + e.getMessage();
        }
    }
    // Insere um novo Livro (PK = fk_Obra_cod_barras)
    public String inserirLivro(String codBarrasObra) {
        String sql = "INSERT INTO Livro (fk_Obra_cod_barras) VALUES (?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codBarrasObra);
            stmt.executeUpdate();
            return "Livro inserido com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir livro: " + e.getMessage();
        }
    }

    // Insere um novo Artigo (PK = fk_Obra_cod_barras)
    public String inserirArtigo(String codBarrasObra) {
        String sql = "INSERT INTO Artigo (fk_Obra_cod_barras) VALUES (?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codBarrasObra);
            stmt.executeUpdate();
            return "Artigo inserido com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir artigo: " + e.getMessage();
        }
    }

    // Insere uma nova Edição (PK composta: id + fk_livro_cod_barras)
    public String inserirEdicao(String idEdicao, String codBarrasLivro) {
        String sql = "INSERT INTO Edicao (id, fk_livro_cod_barras) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idEdicao);
            stmt.setString(2, codBarrasLivro);
            stmt.executeUpdate();
            return "Edição inserida com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir edição: " + e.getMessage();
        }
    }
    // Insere uma nova Estante (PK = numeracao, com atributo prateleira)
    public String inserirEstante(String numeracao, String prateleira) {
        String sql = "INSERT INTO Estante (numeracao, prateleira) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numeracao);
            stmt.setString(2, prateleira);
            stmt.executeUpdate();
            return "Estante inserida com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir estante: " + e.getMessage();
        }
    }

    // Insere um novo Exemplar (PK = id; FK para edição ou artigo, e FK para estante)
    public String inserirExemplar(String idExemplar,
                                  String fkEdicao,    // pode ser null se for artigo
                                  String fkArtigo,    // pode ser null se for edição
                                  String fkEstante) {
        String sql = """
        INSERT INTO Exemplar
            (id, fk_edicao, fk_artigo, fk_estante)
        VALUES (?, ?, ?, ?)
        """;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idExemplar);
            // se for null, o JDBC vai inserir NULL
            if (fkEdicao != null) stmt.setString(2, fkEdicao);
            else               stmt.setNull(2, Types.VARCHAR);

            if (fkArtigo != null) stmt.setString(3, fkArtigo);
            else                 stmt.setNull(3, Types.VARCHAR);

            stmt.setString(4, fkEstante);
            stmt.executeUpdate();
            return "Exemplar inserido com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir exemplar: " + e.getMessage();
        }
    }
    // Insere um novo Telefone (PK = telefone_PK, atributo telefone como INT)
    public String inserirTelefone(String telefonePK, int telefone) {
        String sql = "INSERT INTO Telefone (telefone_PK, telefone) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, telefonePK);
            stmt.setInt(2, telefone);
            stmt.executeUpdate();
            return "Telefone inserido com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir telefone: " + e.getMessage();
        }
    }

    // Insere uma nova Pessoa
// (numero INT, CEP INT, complemento VARCHAR, matricula VARCHAR PK, fk_telefone VARCHAR)
    public String inserirPessoa(int numero,
                                int cep,
                                String complemento,
                                String matricula,
                                String fkTelefone) {
        String sql = """
        INSERT INTO Pessoa
            (numero, CEP, complemento, matricula, fk_telefone)
        VALUES (?, ?, ?, ?, ?)
        """;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, numero);
            stmt.setInt(2, cep);
            stmt.setString(3, complemento);
            stmt.setString(4, matricula);
            stmt.setString(5, fkTelefone);
            stmt.executeUpdate();
            return "Pessoa inserida com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir pessoa: " + e.getMessage();
        }
    }
    // Insere um novo Cliente (PK = fk_Pessoa, atributo historico VARCHAR)
    public String inserirCliente(String fkPessoa, String historico) {
        String sql = "INSERT INTO Cliente (fk_Pessoa, historico) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, fkPessoa);
            stmt.setString(2, historico);
            stmt.executeUpdate();
            return "Cliente inserido com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir cliente: " + e.getMessage();
        }
    }

    // Insere um novo Funcionário (PK e FK = fk_Pessoa)
    public String inserirFuncionario(String fkPessoa) {
        String sql = "INSERT INTO Funcionario (fk_Pessoa) VALUES (?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, fkPessoa);
            stmt.executeUpdate();
            return "Funcionário inserido com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir funcionário: " + e.getMessage();
        }
    }

    // Insere um novo Empréstimo/Aluguel
// (PK = id VARCHAR, atributos hora, data_prevista_dev, data_devolucao, data_emprestimo: TIME;
//  FK para Exemplar e Cliente)
    public String inserirEmprestimoAluga(
            String id,
            java.sql.Time      hora,
            java.sql.Timestamp dataPrevistaDev,  // ← datetime
            java.sql.Date      dataDevolucao,    // ← date
            java.sql.Date      dataEmprestimo,   // ← date
            String             fkExemplar,
            String             fkCliente
    ) {
        String sql = """
    INSERT INTO Emprestimo_aluga
      (id, hora, data_prevista_dev, data_devolucao, data_emprestimo, fk_exemplar, fk_cliente)
    VALUES (?, ?, ?, ?, ?, ?, ?)
    """;
        try (Connection c = DriverManager.getConnection(URL,USER,PASSWORD);
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setString (1, id);
            s.setTime   (2, hora);
            s.setTimestamp(3, dataPrevistaDev);
            s.setDate   (4, dataDevolucao);
            s.setDate   (5, dataEmprestimo);
            s.setString (6, fkExemplar);
            s.setString (7, fkCliente);
            s.executeUpdate();
            return "Empréstimo/Aluguel inserido com sucesso!";
        } catch(SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir Empréstimo/Aluguel: " + e.getMessage();
        }
    }
    // Insere um novo registro em Altera
// (PK composta por fk_funcionario e fk_emprestimo_aluga; data_alteracao TIMESTAMP)
    public String inserirAltera(String fkFuncionario,
                                String fkEmprestimoAluga,
                                java.sql.Timestamp dataAlteracao) {
        String sql = """
        INSERT INTO Altera
            (fk_funcionario, fk_emprestimo_aluga, data_alteracao)
        VALUES (?, ?, ?)
        """;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, fkFuncionario);
            stmt.setString(2, fkEmprestimoAluga);
            stmt.setTimestamp(3, dataAlteracao);

            stmt.executeUpdate();
            return "Altera inserido com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir altera: " + e.getMessage();
        }
    }



    public String deletarEditoraPorNome(String nome) {
        String sql = "DELETE FROM Editora WHERE nome = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return "Editora removida com sucesso!";
            } else {
                return "Nenhuma editora encontrada com nome: " + nome;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar editora: " + e.getMessage();
        }
    }

    public List<String> listarEmprestimos() {
        List<String> resultado = new ArrayList<>();
        String sql = """
            SELECT id,
                   hora,
                   data_prevista_dev,
                   data_devolucao,
                   data_emprestimo,
                   fk_exemplar,
                   fk_cliente
              FROM Emprestimo_aluga
            """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String linha = String.format(
                        "ID: %s, Hora: %s, Prevista: %s, Devolução: %s, Empréstimo: %s, Exemplar: %s, Cliente: %s",
                        rs.getString("id"),
                        rs.getTime("hora"),
                        rs.getTime("data_prevista_dev"),
                        rs.getTime("data_devolucao"),
                        rs.getTime("data_emprestimo"),
                        rs.getString("fk_exemplar"),
                        rs.getString("fk_cliente")
                );
                resultado.add(linha);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    /**
     * Deleta um empréstimo/aluguel pelo seu ID.
     * @param id o identificador do registro em Emprestimo_aluga
     * @return mensagem de sucesso ou erro
     */
    public String deletarEmprestimo(String id) {
        String sql = "DELETE FROM Emprestimo_aluga WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                return "Empréstimo deletado com sucesso!";
            } else {
                return "Nenhum empréstimo encontrado com id: " + id;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar empréstimo: " + e.getMessage();
        }
    }

    // Insere um novo Empréstimo/Aluguel
// (PK = id VARCHAR, atributos hora, data_prevista_dev, data_devolucao, data_emprestimo: TIME;
//  FK para Exemplar e Cliente)
    public String inserirEmprestimoAluga(
            String id,
            java.sql.Time hora,
            java.sql.Time dataPrevistaDev,
            java.sql.Time dataDevolucao,
            java.sql.Time dataEmprestimo,
            String fkExemplar,
            String fkCliente
    ) {
        String sql = """
        INSERT INTO Emprestimo_aluga
          (id, hora, data_prevista_dev, data_devolucao, data_emprestimo, fk_exemplar, fk_cliente)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, id);
            stmt.setTime(2, hora);
            stmt.setTime(3, dataPrevistaDev);
            stmt.setTime(4, dataDevolucao);
            stmt.setTime(5, dataEmprestimo);
            stmt.setString(6, fkExemplar);
            stmt.setString(7, fkCliente);
            stmt.executeUpdate();
            return "Empréstimo/Aluguel inserido com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir Empréstimo/Aluguel: " + e.getMessage();
        }
    }

    public String atualizarEditora(String id, String novoNome) {
        String sql = "UPDATE Editora SET nome = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoNome);
            stmt.setString(2, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return "Editora atualizada com sucesso!";
            } else {
                return "Editora não encontrada para o id: " + id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao atualizar editora: " + e.getMessage();
        }
    }

    public String atualizarAutor(String id, String novoNome) {
        String sql = "UPDATE Autor SET nome = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoNome);
            stmt.setString(2, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return "Autor atualizado com sucesso!";
            } else {
                return "Autor não encontrado para o id: " + id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao atualizar autor: " + e.getMessage();
        }
    }

    public String atualizarGenero(String nomeAntigo, String novoNome) {
        String sql = "UPDATE Genero SET nome = ? WHERE nome = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoNome);
            stmt.setString(2, nomeAntigo);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                return "Gênero atualizado com sucesso!";
            } else {
                return "Gênero não encontrado para o nome: " + nomeAntigo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao atualizar gênero: " + e.getMessage();
        }
    }

    public String deletarLivro(String codBarrasObra) {
        String sql = "DELETE FROM Livro WHERE fk_Obra_cod_barras = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codBarrasObra);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                return "Livro deletado com sucesso!";
            } else {
                return "Livro não encontrado para cod_barras: " + codBarrasObra;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar livro: " + e.getMessage();
        }
    }

    public String visualizarLivros() {
        String sql = """
        SELECT L.fk_Obra_cod_barras AS cod_barras,
               O.titulo,
               O.ano_lanc
          FROM Livro L
          JOIN Obra O
            ON L.fk_Obra_cod_barras = O.cod_barras
        """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Cod Barras: ").append(rs.getString("cod_barras"))
                        .append(", Título: ").append(rs.getString("titulo"))
                        .append(", Ano: ").append(rs.getDate("ano_lanc"))
                        .append("\n");
            }
            return sb.length() > 0
                    ? sb.toString()
                    : "Nenhum livro cadastrado.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao visualizar livros: " + e.getMessage();
        }
    }

    // ARTIGO
    public String atualizarArtigo(String codBarrasAntigo, String novoCodBarras) {
        String sql = "UPDATE Artigo SET fk_Obra_cod_barras = ? WHERE fk_Obra_cod_barras = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoCodBarras);
            stmt.setString(2, codBarrasAntigo);

            int rows = stmt.executeUpdate();
            if (rows > 0) return "Artigo atualizado com sucesso!";
            else           return "Artigo não encontrado para cod_barras: " + codBarrasAntigo;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao atualizar artigo: " + e.getMessage();
        }
    }

    public String deletarArtigo(String codBarrasObra) {
        String sql = "DELETE FROM Artigo WHERE fk_Obra_cod_barras = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codBarrasObra);
            int rows = stmt.executeUpdate();
            if (rows > 0) return "Artigo deletado com sucesso!";
            else           return "Artigo não encontrado para cod_barras: " + codBarrasObra;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar artigo: " + e.getMessage();
        }
    }

    public String visualizarArtigos() {
        String sql = "SELECT fk_Obra_cod_barras FROM Artigo";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Artigo (cod_barras): ")
                        .append(rs.getString("fk_Obra_cod_barras"))
                        .append("\n");
            }
            return sb.length() > 0 ? sb.toString() : "Nenhum artigo cadastrado.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao visualizar artigos: " + e.getMessage();
        }
    }


    // EDIÇÃO
    public String atualizarEdicao(String idEdicaoAntigo, String codBarrasLivro, String novoIdEdicao) {
        String sql = "UPDATE Edicao SET id = ? WHERE id = ? AND fk_livro_cod_barras = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoIdEdicao);
            stmt.setString(2, idEdicaoAntigo);
            stmt.setString(3, codBarrasLivro);

            int rows = stmt.executeUpdate();
            if (rows > 0) return "Edição atualizada com sucesso!";
            else           return "Edição não encontrada para id: " + idEdicaoAntigo;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao atualizar edição: " + e.getMessage();
        }
    }

    public String deletarEdicao(String idEdicao, String codBarrasLivro) {
        String sql = "DELETE FROM Edicao WHERE id = ? AND fk_livro_cod_barras = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idEdicao);
            stmt.setString(2, codBarrasLivro);
            int rows = stmt.executeUpdate();
            if (rows > 0) return "Edição deletada com sucesso!";
            else           return "Edição não encontrada para id: " + idEdicao;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar edição: " + e.getMessage();
        }
    }

    public String visualizarEdicoes() {
        String sql = "SELECT id, fk_livro_cod_barras FROM Edicao";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Edição ID: ").append(rs.getString("id"))
                        .append(", Livro cod_barras: ").append(rs.getString("fk_livro_cod_barras"))
                        .append("\n");
            }
            return sb.length() > 0 ? sb.toString() : "Nenhuma edição cadastrada.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao visualizar edições: " + e.getMessage();
        }
    }


    // ESTANTE
    public String atualizarEstante(String numeracao, String novaPrateleira) {
        String sql = "UPDATE Estante SET prateleira = ? WHERE numeracao = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novaPrateleira);
            stmt.setString(2, numeracao);

            int rows = stmt.executeUpdate();
            if (rows > 0) return "Estante atualizada com sucesso!";
            else           return "Estante não encontrada para numeracao: " + numeracao;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao atualizar estante: " + e.getMessage();
        }
    }

    public String visualizarEstantes() {
        String sql = "SELECT numeracao, prateleira FROM Estante";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Numeracao: ").append(rs.getString("numeracao"))
                        .append(", Prateleira: ").append(rs.getString("prateleira"))
                        .append("\n");
            }
            return sb.length() > 0 ? sb.toString() : "Nenhuma estante cadastrada.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao visualizar estantes: " + e.getMessage();
        }
    }


    // EXEMPLAR
    public String atualizarExemplar(String idExemplar,
                                    String novoFkEdicao,
                                    String novoFkArtigo,
                                    String novoFkEstante) {
        String sql = """
        UPDATE Exemplar
           SET fk_edicao = ?, fk_artigo = ?, fk_estante = ?
         WHERE id = ?
        """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (novoFkEdicao != null) stmt.setString(1, novoFkEdicao);
            else                       stmt.setNull(1, Types.VARCHAR);

            if (novoFkArtigo != null) stmt.setString(2, novoFkArtigo);
            else                      stmt.setNull(2, Types.VARCHAR);

            stmt.setString(3, novoFkEstante);
            stmt.setString(4, idExemplar);

            int rows = stmt.executeUpdate();
            if (rows > 0) return "Exemplar atualizado com sucesso!";
            else           return "Exemplar não encontrado para id: " + idExemplar;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao atualizar exemplar: " + e.getMessage();
        }
    }

    public String deletarExemplar(String idExemplar) {
        String sql = "DELETE FROM Exemplar WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idExemplar);
            int rows = stmt.executeUpdate();
            if (rows > 0) return "Exemplar deletado com sucesso!";
            else           return "Exemplar não encontrado para id: " + idExemplar;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar exemplar: " + e.getMessage();
        }
    }

    public String visualizarExemplares() {
        String sql = "SELECT id, fk_edicao, fk_artigo, fk_estante FROM Exemplar";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getString("id"))
                        .append(", Edição: ").append(rs.getString("fk_edicao"))
                        .append(", Artigo: ").append(rs.getString("fk_artigo"))
                        .append(", Estante: ").append(rs.getString("fk_estante"))
                        .append("\n");
            }
            return sb.length() > 0 ? sb.toString() : "Nenhum exemplar cadastrado.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao visualizar exemplares: " + e.getMessage();
        }
    }
    // CLIENTE
    public String atualizarCliente(String fkPessoa, String novoHistorico) {
        String sql = "UPDATE Cliente SET historico = ? WHERE fk_Pessoa = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoHistorico);
            stmt.setString(2, fkPessoa);
            int rows = stmt.executeUpdate();

            if (rows > 0) return "Cliente atualizado com sucesso!";
            else           return "Cliente não encontrado para fk_Pessoa: " + fkPessoa;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao atualizar cliente: " + e.getMessage();
        }
    }

    public String deletarCliente(String fkPessoa) {
        String sql = "DELETE FROM Cliente WHERE fk_Pessoa = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fkPessoa);
            int rows = stmt.executeUpdate();

            if (rows > 0) return "Cliente deletado com sucesso!";
            else           return "Cliente não encontrado para fk_Pessoa: " + fkPessoa;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar cliente: " + e.getMessage();
        }
    }

    public String visualizarClientes() {
        String sql = "SELECT fk_Pessoa, historico FROM Cliente";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Pessoa: ").append(rs.getString("fk_Pessoa"))
                        .append(", Histórico: ").append(rs.getString("historico"))
                        .append("\n");
            }
            return sb.length() > 0 ? sb.toString() : "Nenhum cliente cadastrado.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao visualizar clientes: " + e.getMessage();
        }
    }


    // FUNCIONÁRIO
    public String atualizarFuncionario(String fkPessoaAntigo, String novoFkPessoa) {
        String sql = "UPDATE Funcionario SET fk_Pessoa = ? WHERE fk_Pessoa = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoFkPessoa);
            stmt.setString(2, fkPessoaAntigo);
            int rows = stmt.executeUpdate();

            if (rows > 0) return "Funcionário atualizado com sucesso!";
            else           return "Funcionário não encontrado para fk_Pessoa: " + fkPessoaAntigo;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao atualizar funcionário: " + e.getMessage();
        }
    }

    public String deletarFuncionario(String fkPessoa) {
        String sql = "DELETE FROM Funcionario WHERE fk_Pessoa = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fkPessoa);
            int rows = stmt.executeUpdate();

            if (rows > 0) return "Funcionário deletado com sucesso!";
            else           return "Funcionário não encontrado para fk_Pessoa: " + fkPessoa;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar funcionário: " + e.getMessage();
        }
    }

    public String visualizarFuncionarios() {
        String sql = "SELECT fk_Pessoa FROM Funcionario";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Funcionário (Pessoa): ").append(rs.getString("fk_Pessoa")).append("\n");
            }
            return sb.length() > 0 ? sb.toString() : "Nenhum funcionário cadastrado.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao visualizar funcionários: " + e.getMessage();
        }
    }


    // EMPRÉSTIMO_ALUGA
    public String atualizarEmprestimoAluga(
            String id,
            java.sql.Time hora,
            java.sql.Time dataPrevistaDev,
            java.sql.Time dataDevolucao,
            java.sql.Time dataEmprestimo,
            String fkExemplar,
            String fkCliente
    ) {
        String sql = """
        UPDATE Emprestimo_aluga
           SET hora = ?, data_prevista_dev = ?, data_devolucao = ?, data_emprestimo = ?, fk_exemplar = ?, fk_cliente = ?
         WHERE id = ?
        """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTime(1, hora);
            stmt.setTime(2, dataPrevistaDev);
            stmt.setTime(3, dataDevolucao);
            stmt.setTime(4, dataEmprestimo);
            stmt.setString(5, fkExemplar);
            stmt.setString(6, fkCliente);
            stmt.setString(7, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) return "Empréstimo/Aluguel atualizado com sucesso!";
            else           return "Empréstimo/Aluguel não encontrado para id: " + id;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao atualizar Empréstimo/Aluguel: " + e.getMessage();
        }
    }

    public String deletarEmprestimoAluga(String id) {
        String sql = "DELETE FROM Emprestimo_aluga WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            int rows = stmt.executeUpdate();

            if (rows > 0) return "Empréstimo/Aluguel deletado com sucesso!";
            else           return "Empréstimo/Aluguel não encontrado para id: " + id;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar Empréstimo/Aluguel: " + e.getMessage();
        }
    }

    public String visualizarEmprestimosAluga() {
        String sql = """
        SELECT id, hora, data_prevista_dev, data_devolucao, data_emprestimo, fk_exemplar, fk_cliente
          FROM Emprestimo_aluga
        """;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getString("id"))
                        .append(", Hora: ").append(rs.getTime("hora"))
                        .append(", Prevista Dev.: ").append(rs.getTime("data_prevista_dev"))
                        .append(", Devolução: ").append(rs.getTime("data_devolucao"))
                        .append(", Empréstimo: ").append(rs.getTime("data_emprestimo"))
                        .append(", Exemplar: ").append(rs.getString("fk_exemplar"))
                        .append(", Cliente: ").append(rs.getString("fk_cliente"))
                        .append("\n");
            }
            return sb.length() > 0 ? sb.toString() : "Nenhum empréstimo/aluguel cadastrado.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao visualizar Empréstimos/Aluguéis: " + e.getMessage();
        }
    }


    // ALTERA
    public String visualizarAlteras() {
        String sql = "SELECT fk_funcionario, fk_emprestimo_aluga, data_alteracao FROM Altera";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Funcionário: ").append(rs.getString("fk_funcionario"))
                        .append(", Empréstimo: ").append(rs.getString("fk_emprestimo_aluga"))
                        .append(", Data Alteração: ").append(rs.getTimestamp("data_alteracao"))
                        .append("\n");
            }
            return sb.length() > 0 ? sb.toString() : "Nenhum registro de Altera.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao visualizar Altera: " + e.getMessage();
        }
    }


}