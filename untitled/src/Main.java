import java.sql.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Teste!");
        //con = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdatenbank", "root", "adminadmin123");
        String url = "jdbc:mysql://localhost:3306/biblioteca";
        String user = "root";
        String password = "12345";

        try{
            Connection conn = DriverManager.getConnection(url,user,password);
            System.out.println("Connected!");
            //This is just a test
            String createTable = "CREATE TABLE IF NOT EXISTS obras (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(100) NOT NULL) ";

            String insert = "INSERT INTO obras (name) VALUES (?)";
            PreparedStatement insertStatement = conn.prepareStatement(insert);
            insertStatement.setString(1,"Obra1");
            insertStatement.executeUpdate();
            System.out.println("Obra inserida");


            // Read test
            String selectSql = "SELECT * FROM obras";
            ResultSet result = conn.createStatement().executeQuery(selectSql);
            while( result.next() ){
                System.out.println(result);
                System.out.println("ID: " + result.getInt("id") + ", Name: " + result.getString("name"));
            }

            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e); // optional catch
        }
    }
}