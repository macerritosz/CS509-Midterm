import java.sql.*;

public class DatabaseSchema {
    public static void createTable(Connection dbConnection){
        String tableSchema = "CREATE TABLE IF NOT EXISTS accounts ("
                + "account_id INTEGER PRIMARY KEY AUTO_INCREMENT, "
                + "holder VARCHAR(50) NOT NULL, "
                + "balance FLOAT NOT NULL, "
                + "login VARCHAR(50) NOT NULL UNIQUE, "
                + "pin CHAR(5) NOT NULL, "
                + "status ENUM('ACTIVE', 'DISABLED') NOT NULL DEFAULT 'ACTIVE',"
                + "account_type ENUM('ADMIN', 'CUSTOMER') NOT NULL DEFAULT 'CUSTOMER'"
                + " ); ";
        try {
            Statement statement = dbConnection.createStatement();
            statement.executeUpdate(tableSchema);
            System.out.println("Accounts Table created");
        }catch ( Exception e ){
            e.printStackTrace();
        }
    }
}
