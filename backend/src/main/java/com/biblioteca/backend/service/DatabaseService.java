package com.biblioteca.backend.service;

import com.biblioteca.backend.dto.ClienteDTO;
import com.biblioteca.backend.dto.EmprestimoDTO;
import com.biblioteca.backend.dto.ExemplarDTO;
import com.biblioteca.backend.dto.ObraDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@Service
public class DatabaseService {

    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca"; // jdbc is necessary for the protocol identifier
    private static final String USER = "root";
    private static final String PASSWORD = "12345";


    // Method to execute SQL script (like schema.sql)
    public void criarTriggerEFunction() {
        String function = """
        CREATE FUNCTION dias_ate_devolucao(idEmprestimo VARCHAR(50)) RETURNS INT
        DETERMINISTIC
        BEGIN
            DECLARE dias INT;
            SELECT DATEDIFF(DATE(data_prevista_dev), CURDATE())
            INTO dias
            FROM Emprestimo_aluga
            WHERE id = idEmprestimo;
            RETURN dias;
        END
        """;

        String trigger = """
        CREATE TRIGGER verificar_exemplar_disponivel
        BEFORE INSERT ON Emprestimo_aluga
        FOR EACH ROW
        BEGIN
            DECLARE exemplar_em_uso INT DEFAULT 0;
            SELECT COUNT(*) INTO exemplar_em_uso
            FROM emprestimo_aluga ea
            WHERE fk_exemplar = NEW.fk_exemplar AND data_devolucao IS NULL;

            IF exemplar_em_uso > 0 THEN
                SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Exemplar já está emprestado e não devolvido.';
            END IF;
        END
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Primeiro, tentar remover a função e trigger se já existirem.
            try {
                stmt.execute("DROP FUNCTION IF EXISTS dias_ate_devolucao");
                System.out.println("Função antiga removida (se existia).");
            } catch (SQLException e) {
                System.out.println("Erro ao remover função: " + e.getMessage());
            }

            try {
                stmt.execute("DROP TRIGGER IF EXISTS verificar_exemplar_disponivel");
                System.out.println("Trigger antiga removida (se existia).");
            } catch (SQLException e) {
                System.out.println("Erro ao remover trigger: " + e.getMessage());
            }

            // Agora criar a função
            stmt.execute(function);
            System.out.println("Função criada com sucesso!");

            // Agora criar a trigger
            stmt.execute(trigger);
            System.out.println("Trigger criada com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao criar trigger ou função: " + e.getMessage());
            e.printStackTrace();
        }
    }


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


    public Map<String,Integer> contarEmprestimosPorCliente() {
        String sql = """
                SELECT pessoa.nome, COUNT(*) AS total
                FROM emprestimo_aluga
                JOIN cliente ON emprestimo_aluga.fk_cliente = cliente.id
                JOIN pessoa ON cliente.fk_Pessoa_id = pessoa.id
                GROUP BY pessoa.nome;
                """;

        Map<String, Integer> emprestimosPorCliente = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nomeCliente = rs.getString("nome");
                int totalEmprestimos = rs.getInt("total");
                emprestimosPorCliente.put(nomeCliente, totalEmprestimos);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao contar empréstimos por cliente: " + e.getMessage());
        }
        return emprestimosPorCliente;
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

    public List<ClienteDTO> visualizarClientes() {
        List<ClienteDTO> clientes = new ArrayList<>();

        String sql = "SELECT c.id, c.historico, c.fk_Pessoa_id, p.nome " +
                "FROM cliente c " +
                "JOIN pessoa p ON c.fk_Pessoa_id = p.id";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ClienteDTO cliente = new ClienteDTO();
                cliente.setId(rs.getString("id"));
                cliente.setHistorico(rs.getString("historico"));
                cliente.setFkPessoaId(rs.getString("fk_pessoa_id"));
                cliente.setNomePessoa(rs.getString("nome")); // Adicione este campo na sua ClienteDTO

                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }


    // Method to insert a new "Obra"
    public String inserirObra(String codBarras, String titulo, java.sql.Date ano) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            String sql = "INSERT INTO Obra (cod_barras, titulo, ano_lanc) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, codBarras);
                stmt.setString(2, titulo);
                stmt.setDate(3, ano);
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
    public String atualizarObra(String cod_barras, ObraDTO dto) {
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            String sql = "UPDATE obra SET titulo = ?, ano_lanc = ? WHERE cod_barras = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setString(1, dto.getTitulo());
                stmt.setDate(2, dto.getAno_lanc());
                stmt.setString(3, cod_barras);// Or dto.cod_barras I believe. Actually it won't come with any cod_barras.
                int linesThatHaveBeenAffected = stmt.executeUpdate();

                if (linesThatHaveBeenAffected > 0){
                    return "Obra atualizada com sucesso!";
                } else{
                    return "Nenhuma obra encontrada para atualizar.";
                }
            }


        } catch (SQLException e){
            e.printStackTrace();
            return "Erro ao ATUALIZAR a obra: " + e.getMessage();
        }

    }

    public ObraDTO buscarObraPorCodBarras(String codBarras) {
        System.out.println("Buscando obra com código de barras: " + codBarras);
        String sql = "SELECT cod_barras, titulo, ano_lanc FROM obra WHERE cod_barras = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, codBarras);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ObraDTO obra = new ObraDTO();
                    obra.setCod_barras(rs.getString("cod_barras"));
                    obra.setTitulo(rs.getString("titulo"));

                    java.sql.Date sqlDate = rs.getDate("ano_lanc");
                    if (sqlDate != null) {
                        obra.setAno_lanc(sqlDate);
                    }

                    return obra;
                } else {
                    // Obra não encontrada — lançar exceção
                    throw new RuntimeException("Obra não encontrada com código de barras: " + codBarras);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar obra no banco de dados", e);
        }
    }


    // Method to select all "Obras"
    public List<ObraDTO> visualizarObras() {
        List<ObraDTO> obras = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM obra";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                var resultSet = stmt.executeQuery();

                //StringBuilder result = new StringBuilder(); My old implementation
                while (resultSet.next()) {
                    ObraDTO obra = new ObraDTO();
                    obra.setCod_barras(resultSet.getString("cod_barras"));
                    obra.setTitulo(resultSet.getString("titulo"));
                    obra.setAno_lanc(resultSet.getDate("ano_lanc") != null ? resultSet.getDate("ano_lanc") : null);
                    obras.add(obra);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Problema ao visualizarObras!");
        }
        return obras; // Returning the whoooole list
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

    public String deletarObraPorCodBarras(String cod_barras){
        String string = "DELETE FROM obra WHERE cod_barras = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            PreparedStatement stmt = connection.prepareStatement(string);
            stmt.setString(1,cod_barras);
            stmt.executeUpdate();
            System.out.println("DeletarObraPorCodBarra rodou!");
            return "DeletarObraPorCodBarra rodou!";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Something went wrong when trying deletarObraPorCodBarras";
    }


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

    public ExemplarDTO buscarExemplarPorId(String id) {
        String sql = "SELECT id, fk_edicao, fk_artigo, fk_estante_prateleira, fk_estante_numero " +
                "FROM Exemplar WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ExemplarDTO exemplar = new ExemplarDTO();
                    exemplar.setId(rs.getString("id"));
                    exemplar.setFkEdicao(rs.getString("fk_edicao"));
                    exemplar.setFkArtigo(rs.getString("fk_artigo"));
                    exemplar.setFkEstantePrateleira(rs.getString("fk_estante_prateleira"));
                    exemplar.setFkEstanteNumero(rs.getString("fk_estante_numero"));
                    return exemplar;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if not found or error occurs
    }

    public List<ExemplarDTO> listarExemplares() {
        List<ExemplarDTO> exemplares = new ArrayList<>();

        String sql = """
        SELECT e.id, e.fk_edicao,
                                   e.fk_artigo,
                                   e.fk_estante_prateleira,
                                   e.fk_estante_numero,
                                   o.titulo AS nome_obra
                            FROM Exemplar e
                            JOIN Obra o ON e.fk_obra_cod_barras = o.cod_barras;
    """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ExemplarDTO exemplar = new ExemplarDTO();
                exemplar.setId(rs.getString("id"));
                exemplar.setFkEdicao(rs.getString("fk_edicao"));
                exemplar.setFkArtigo(rs.getString("fk_artigo"));
                exemplar.setFkEstantePrateleira(rs.getString("fk_estante_prateleira"));
                exemplar.setFkEstanteNumero(rs.getString("fk_estante_numero"));
                exemplar.setNomeObra(rs.getString("nome_obra")); // <- novo campo

                exemplares.add(exemplar);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exemplares;
    }


    // Insere um novo Exemplar (PK = id; FK para edição ou artigo, e FK para estante)
    public String inserirExemplar(String idExemplar, ExemplarDTO dto) {
        String sql = """
        INSERT INTO Exemplar
            (id, fk_obra_cod_barras, fk_edicao, fk_artigo, fk_estante_prateleira, fk_estante_numero)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, idExemplar);

            stmt.setString(2, dto.getFkObraCodBarras()); // novo campo

            if (dto.getFkEdicao() != null && !dto.getFkEdicao().isEmpty()) { // We need to verify if the object exists first before checking if its empty
                stmt.setString(3, dto.getFkEdicao());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }

            if (dto.getFkArtigo() != null && !dto.getFkArtigo().isEmpty()) {
                stmt.setString(4, dto.getFkArtigo());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }

            if (dto.getFkEstantePrateleira() != null && !dto.getFkEstantePrateleira().isEmpty()) {
                stmt.setString(5, dto.getFkEstantePrateleira());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }

            stmt.setString(6, dto.getFkEstanteNumero());
            if (dto.getFkEstanteNumero() != null && !dto.getFkEstanteNumero().isEmpty()) {
                stmt.setString(6, dto.getFkEstantePrateleira());
            } else {
                stmt.setNull(6, Types.VARCHAR);
            }

            stmt.executeUpdate();
            return "Exemplar inserido com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir exemplar: " + e.getMessage();
        }
    }



    public String alterarExemplar(String id, ExemplarDTO dto) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sqlUpdate = """
        UPDATE Exemplar 
        SET fk_edicao = ?, 
            fk_artigo = ?, 
            fk_estante_prateleira = ?, 
            fk_estante_numero = ?
        WHERE id = ?;
        """;

            try (PreparedStatement stmt = connection.prepareStatement(sqlUpdate)) {
                stmt.setString(1, dto.getFkEdicao());
                stmt.setString(2, dto.getFkArtigo());
                stmt.setString(3, dto.getFkEstantePrateleira());
                stmt.setString(4, dto.getFkEstanteNumero());
                stmt.setString(5, id);

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected == 0) {
                    return "Nenhum exemplar encontrado com o ID fornecido.";
                }
            }

            return "Exemplar atualizado com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao atualizar exemplar: " + e.getMessage();
        }
    }

    public String deletarExemplar(String id) {
        String sql = "DELETE FROM Exemplar WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, id);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0
                    ? "Exemplar deletado com sucesso!"
                    : "Nenhum exemplar encontrado com o ID: " + id;

        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar exemplar: " + e.getMessage();
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
//  FK para Exemplar e Cliente) CHANGE THIS LATER
    public String inserirEmprestimoAluga(
            Timestamp dataPrevistaDev,
            java.sql.Date dataDevolucao,
            Timestamp dataEmprestimo,
            String fkExemplar,
            String fkCliente,
            String fkFuncionario) {

        String id = "emp-" + UUID.randomUUID(); // gerar ID AQUI

        String sql = """
    INSERT INTO Emprestimo_aluga
      (id, data_prevista_dev, data_devolucao, data_emprestimo, 
       fk_exemplar, fk_cliente, fk_funcionario)
    VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection c = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement s = c.prepareStatement(sql)) {

            s.setString(1, id);
            s.setTimestamp(2, dataPrevistaDev);
            s.setDate(3, dataDevolucao);
            s.setTimestamp(4, dataEmprestimo);
            s.setString(5, fkExemplar);
            s.setString(6, fkCliente);
            s.setString(7, fkFuncionario);

            s.executeUpdate();
            return "Empréstimo/Aluguel inserido com sucesso!";
        } catch(SQLException e) {
            e.printStackTrace();
            return "Erro ao inserir Empréstimo/Aluguel: " + e.getMessage();
        }
    }


    public String alterarEmprestimo(
            String id,
            Timestamp dataPrevistaDev,
            java.sql.Date dataDevolucao,
            Timestamp dataEmprestimo,
            String fkExemplar,
            String fkCliente,
            String fkFuncionario) {

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sqlUpdate = """
            UPDATE Emprestimo_aluga 
            SET data_prevista_dev = ?, 
                data_devolucao = ?, 
                data_emprestimo = ?, 
                fk_exemplar = ?, 
                fk_cliente = ?,
                fk_funcionario = ?
            WHERE id = ?;
            """;

            try (PreparedStatement stmt = connection.prepareStatement(sqlUpdate)) {
                stmt.setTimestamp(1, dataPrevistaDev);
                stmt.setDate(2, dataDevolucao);
                stmt.setTimestamp(3, dataEmprestimo);
                stmt.setString(4, fkExemplar);
                stmt.setString(5, fkCliente);
                stmt.setString(6, fkFuncionario);  // Added funcionario field
                stmt.setString(7, id);

                stmt.executeUpdate();
            }

            return "Empréstimo atualizado com sucesso!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao atualizar empréstimo: " + e.getMessage();
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

    public List<EmprestimoDTO> listarEmprestimosDTO() {
        List<EmprestimoDTO> emprestimos = new ArrayList<>();

        String sql = """
        SELECT ea.id,
               ea.data_prevista_dev,
               ea.data_devolucao,
               ea.data_emprestimo,
               ea.fk_exemplar,
               ea.fk_cliente,
               ea.fk_funcionario,
               COALESCE(ob_livro.titulo, ob_artigo.titulo) AS nome_exemplar,
               p.nome AS nome_cliente
        FROM Emprestimo_aluga ea
        JOIN Exemplar ex ON ea.fk_exemplar = ex.id

        -- Livro
        LEFT JOIN Edicao ed ON ex.fk_edicao = ed.id
        LEFT JOIN Livro l ON ed.livro_cod_barras = l.fk_Obra_cod_barras
        LEFT JOIN Obra ob_livro ON l.fk_Obra_cod_barras = ob_livro.cod_barras

        -- Artigo
        LEFT JOIN Artigo a ON ex.fk_artigo = a.id
        LEFT JOIN Obra ob_artigo ON a.fk_Obra_cod_barras = ob_artigo.cod_barras

        -- Cliente
        JOIN Cliente c ON ea.fk_cliente = c.id
        JOIN Pessoa p ON c.fk_Pessoa_id = p.id
    """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                EmprestimoDTO dto = new EmprestimoDTO();
                dto.setId(rs.getString("id"));

                // DATETIME -> LocalDateTime
                Timestamp dataPrevistaDev = rs.getTimestamp("data_prevista_dev");
                dto.setDataPrevistaDev(dataPrevistaDev != null ? dataPrevistaDev.toLocalDateTime() : null);

                // DATE -> LocalDate
                Date dataDevolucao = rs.getDate("data_devolucao");
                dto.setDataDevolucao(dataDevolucao != null ? dataDevolucao.toLocalDate() : null);

                // DATETIME -> LocalDateTime
                Timestamp dataEmprestimo = rs.getTimestamp("data_emprestimo");
                dto.setDataEmprestimo(dataEmprestimo != null ? dataEmprestimo.toLocalDateTime() : null);

                dto.setFkExemplar(rs.getString("fk_exemplar"));
                dto.setNomeObra(rs.getString("nome_exemplar"));

                dto.setFkCliente(rs.getString("fk_cliente"));
                dto.setNomeCliente(rs.getString("nome_cliente"));

                dto.setFkFuncionario(rs.getString("fk_funcionario"));

                emprestimos.add(dto);
            }

            System.out.println("Total empréstimos retornados: " + emprestimos.size());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emprestimos;
    }




    public EmprestimoDTO buscarEmprestimoPorId(String id) {
        String sql = """
        SELECT ea.id,
               ea.data_prevista_dev,
               ea.data_devolucao,
               ea.data_emprestimo,
               ea.fk_exemplar,
               ea.fk_cliente,
               ea.fk_funcionario,
               COALESCE(ob_livro.titulo, ob_artigo.titulo) AS nome_exemplar,
               p.nome AS nome_cliente
        FROM Emprestimo_aluga ea
        JOIN Exemplar ex ON ea.fk_exemplar = ex.id

        -- Livro
        LEFT JOIN Edicao ed ON ex.fk_edicao = ed.id
        LEFT JOIN Livro l ON ed.livro_cod_barras = l.fk_Obra_cod_barras
        LEFT JOIN Obra ob_livro ON l.fk_Obra_cod_barras = ob_livro.cod_barras

        -- Artigo
        LEFT JOIN Artigo a ON ex.fk_artigo = a.id
        LEFT JOIN Obra ob_artigo ON a.fk_Obra_cod_barras = ob_artigo.cod_barras

        -- Cliente
        JOIN Cliente c ON ea.fk_cliente = c.id
        JOIN Pessoa p ON c.fk_Pessoa_id = p.id

        WHERE ea.id = ?
    """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    EmprestimoDTO dto = new EmprestimoDTO();

                    dto.setId(rs.getString("id"));

                    Timestamp dataPrevistaDev = rs.getTimestamp("data_prevista_dev");
                    dto.setDataPrevistaDev(dataPrevistaDev != null ? dataPrevistaDev.toLocalDateTime() : null);

                    Date dataDevolucao = rs.getDate("data_devolucao");
                    dto.setDataDevolucao(dataDevolucao != null ? dataDevolucao.toLocalDate() : null);

                    Timestamp dataEmprestimo = rs.getTimestamp("data_emprestimo");
                    dto.setDataEmprestimo(dataEmprestimo != null ? dataEmprestimo.toLocalDateTime() : null);

                    dto.setFkExemplar(rs.getString("fk_exemplar"));
                    dto.setNomeObra(rs.getString("nome_exemplar"));

                    dto.setFkCliente(rs.getString("fk_cliente"));
                    dto.setNomeCliente(rs.getString("nome_cliente"));

                    dto.setFkFuncionario(rs.getString("fk_funcionario"));

                    return dto;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<String> listarEmprestimosPorCliente(String fkCliente) {
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
         WHERE fk_cliente = ?
    """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the fk_cliente parameter in the query
            stmt.setString(1, fkCliente);

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
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


    public String inserirPessoaParaCliente(String nomePessoa, String historicoCliente) {
        Connection connection = null;
        PreparedStatement stmtPessoa = null;
        PreparedStatement stmtCliente = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(false); // serve mesmo?

            // Gerar ID para pessoa
            String idPessoa = UUID.randomUUID().toString();

            //Inserir pessoa
            String sqlPessoa = "INSERT INTO pessoa (id, nome) values (?, ?)";
            stmtPessoa = connection.prepareStatement(sqlPessoa);
            stmtPessoa.setString(1,idPessoa);
            stmtPessoa.setString(2,nomePessoa);
            stmtPessoa.executeUpdate();

            // 3. Gerar ID para Cliente
            String idCliente = UUID.randomUUID().toString();

            // 4. Inserir Cliente vinculado
            String sqlCliente = "INSERT INTO Cliente (id, historico, fk_Pessoa_id) VALUES (?, ?, ?)";
            stmtCliente = connection.prepareStatement(sqlCliente);
            stmtCliente.setString(1, idCliente);
            stmtCliente.setString(2, historicoCliente);
            stmtCliente.setString(3, idPessoa);
            stmtCliente.executeUpdate();

            connection.commit(); // Confirma transação
            return "Cliente cadastrado com sucesso! ID: " + idCliente;
        }
        catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback(); // Em caso de erro, desfaz as operações
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return "Erro ao cadastrar cliente: " + e.getMessage();
        } finally {
            try {
                if (stmtPessoa != null) stmtPessoa.close();
                if (stmtCliente != null) stmtCliente.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public String atualizarClienteEPessoa(String clienteId, String novoNome, String novoHistorico) {
        // Validações iniciais
        if (clienteId == null || clienteId.trim().isEmpty()) {
            return "ID do cliente é obrigatório";
        }
        if (novoNome == null || novoNome.trim().isEmpty()) {
            return "Nome da pessoa é obrigatório";
        }

        // Use try-with-resources para fechar automaticamente
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection.setAutoCommit(false);

            try {
                // 1. Obter ID da Pessoa
                String pessoaId;
                String sqlGetPessoa = "SELECT fk_Pessoa_id FROM Cliente WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sqlGetPessoa)) {
                    stmt.setString(1, clienteId);
                    ResultSet rs = stmt.executeQuery();
                    if (!rs.next()) {
                        System.out.println("Cliente não encontrado");
                        return "Cliente não encontrado";
                    }
                    pessoaId = rs.getString("fk_Pessoa_id");
                }

                // 2. Atualizar Pessoa
                String sqlPessoa = "UPDATE Pessoa SET nome = ? WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sqlPessoa)) {
                    stmt.setString(1, novoNome);
                    stmt.setString(2, pessoaId);
                    if (stmt.executeUpdate() == 0) {
                        connection.rollback();
                        System.out.println("Falha ao atualizar Pessoa;");
                        return "Falha ao atualizar Pessoa";
                    }
                }

                // 3. Atualizar Cliente
                String sqlCliente = "UPDATE Cliente SET historico = ? WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sqlCliente)) {
                    stmt.setString(1, novoHistorico != null ? novoHistorico : "");
                    stmt.setString(2, clienteId);
                    if (stmt.executeUpdate() == 0) {
                        connection.rollback();
                        System.out.println("Falha ao atualizar Cliente;");
                        return "Falha ao atualizar Cliente";
                    }
                }

                connection.commit();
                return "Atualização realizada com sucesso";

            } catch (SQLException e) {
                connection.rollback();
                return "Erro no banco de dados: " + e.getMessage();
            }
        } catch (SQLException e) {
            return "Erro de conexão: " + e.getMessage();
        }
    }


    public String deletarCliente(String clienteId) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // this will leave pessoa intact though :/ will do for now
            String sql = "DELETE FROM Cliente WHERE id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, clienteId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                return "Cliente não encontrado!";
            }
            return "Cliente deletado com sucesso!";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro ao deletar cliente: " + e.getMessage();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Integer calcularDiasAteDevolucao(String idEmprestimo) {
        String sql = "SELECT dias_ate_devolucao(?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idEmprestimo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
