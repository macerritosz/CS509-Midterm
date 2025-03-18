import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection implements DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/midterm";
    private static final String USER = "macerr";
    private static final String PASSWORD = "password";

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
