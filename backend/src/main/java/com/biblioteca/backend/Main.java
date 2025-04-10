import java.sql.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Teste!");
        //con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdatenbank", "root", "adminadmin123");
        // do not forget to create a biblioteca table on your dbeaver!
        String url = "jdbc:mysql://localhost:3306/biblioteca";
        String user = "root";
        String password = "12345";

        try {

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected!");

            Statement stmt = conn.createStatement();

            // First create the independent tables (no foreign keys)
            String createPessoaTable = "CREATE TABLE IF NOT EXISTS Pessoa (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "nome VARCHAR(100), " +
                    "complemento VARCHAR(100), " +
                    "cep VARCHAR(50))";
            stmt.executeUpdate(createPessoaTable);

            String createEditoraTable = "CREATE TABLE IF NOT EXISTS Editora (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL)";
            stmt.executeUpdate(createEditoraTable);

            String createAutorTable = "CREATE TABLE IF NOT EXISTS Autor (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "nome VARCHAR(100) NOT NULL)";
            stmt.executeUpdate(createAutorTable);

            String createGeneroTable = "CREATE TABLE IF NOT EXISTS Genero (" +
                    "nome VARCHAR(50) PRIMARY KEY)";
            stmt.executeUpdate(createGeneroTable);

            String createEstanteTable = "CREATE TABLE IF NOT EXISTS Estante (" +
                    "prateleira VARCHAR(50), " +
                    "numero VARCHAR(50), " +
                    "PRIMARY KEY (prateleira, numero))";
            stmt.executeUpdate(createEstanteTable);

            // Create tables that depend on the above
            String createObraTable = "CREATE TABLE IF NOT EXISTS Obra (" +
                    "cod_barras VARCHAR(50) PRIMARY KEY, " +
                    "titulo VARCHAR(100) NOT NULL, " +
                    "ano_lanc DATE)";
            stmt.executeUpdate(createObraTable);

            String createClienteTable = "CREATE TABLE IF NOT EXISTS Cliente (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "historico VARCHAR(100), " +
                    "fk_Pessoa_id VARCHAR(50), " +
                    "FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa(id))";
            stmt.executeUpdate(createClienteTable);

            String createFuncionarioTable = "CREATE TABLE IF NOT EXISTS Funcionario (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "fk_Pessoa_id VARCHAR(50), " +
                    "FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa(id))";
            stmt.executeUpdate(createFuncionarioTable);

            // Create tables that depend on the previous ones
            String createLivroTable = "CREATE TABLE IF NOT EXISTS Livro (" +
                    "fk_Obra_cod_barras VARCHAR(50), " +
                    "PRIMARY KEY (fk_Obra_cod_barras), " +
                    "FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras))";
            stmt.executeUpdate(createLivroTable);

            String createArtigoTable = "CREATE TABLE IF NOT EXISTS Artigo (" +
                    "fk_Obra_cod_barras VARCHAR(50), " +
                    "titulo VARCHAR(100), " +
                    "PRIMARY KEY (fk_Obra_cod_barras), " +
                    "FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras))";
            stmt.executeUpdate(createArtigoTable);

            String createEmprestimoAlugaTable = "CREATE TABLE IF NOT EXISTS Emprestimo_aluga (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "hora TIME, " +
                    "data_prevista_dev DATE, " +
                    "data_devolucao DATE, " +
                    "fk_Cliente_id VARCHAR(50), " +
                    "FOREIGN KEY (fk_Cliente_id) REFERENCES Cliente(id))";
            stmt.executeUpdate(createEmprestimoAlugaTable);

            String createTelefoneTable = "CREATE TABLE IF NOT EXISTS Telefone (" +
                    "telefone_pk VARCHAR(50) PRIMARY KEY, " +
                    "numero INT, " +
                    "fk_Pessoa_id VARCHAR(50), " +
                    "FOREIGN KEY (fk_Pessoa_id) REFERENCES Pessoa(id))";
            stmt.executeUpdate(createTelefoneTable);

            // Create junction tables (many-to-many relationships)
            String createPublicaTable = "CREATE TABLE IF NOT EXISTS publica (" +
                    "fk_Obra_cod_barras VARCHAR(50), " +
                    "fk_Editora_id VARCHAR(50), " +
                    "PRIMARY KEY (fk_Obra_cod_barras, fk_Editora_id), " +
                    "FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras), " +
                    "FOREIGN KEY (fk_Editora_id) REFERENCES Editora(id))";
            stmt.executeUpdate(createPublicaTable);

            String createEscreveTable = "CREATE TABLE IF NOT EXISTS escreve (" +
                    "fk_Autor_id VARCHAR(50), " +
                    "fk_Obra_cod_barras VARCHAR(50), " +
                    "PRIMARY KEY (fk_Autor_id, fk_Obra_cod_barras), " +
                    "FOREIGN KEY (fk_Autor_id) REFERENCES Autor(id), " +
                    "FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras))";
            stmt.executeUpdate(createEscreveTable);

            String createPertenceTable = "CREATE TABLE IF NOT EXISTS Pertence (" +
                    "fk_Genero_nome VARCHAR(50), " +
                    "fk_Obra_cod_barras VARCHAR(50), " +
                    "PRIMARY KEY (fk_Genero_nome, fk_Obra_cod_barras), " +
                    "FOREIGN KEY (fk_Genero_nome) REFERENCES Genero(nome), " +
                    "FOREIGN KEY (fk_Obra_cod_barras) REFERENCES Obra(cod_barras))";
            stmt.executeUpdate(createPertenceTable);

            String createEdicaoTable = "CREATE TABLE IF NOT EXISTS Edicao (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "livro_cod_barras VARCHAR(50), " +
                    "numero INT, " +
                    "FOREIGN KEY (livro_cod_barras) REFERENCES Livro(fk_Obra_cod_barras))";
            stmt.executeUpdate(createEdicaoTable);

            System.out.println("All tables created successfully in case they don't exist!");

            String selectSql = "SELECT * FROM Obra";
            ResultSet result = stmt.executeQuery(selectSql);
            System.out.println("only printing what's inside Obra");
            while (result.next()) {
                System.out.println("Cod Barras: " + result.getString("cod_barras") +
                        ", Titulo: " + result.getString("titulo"));
            }


            conn.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Probably gotta change this later
        }
    }
}
