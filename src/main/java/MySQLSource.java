import java.sql.*;

public class MySQLSource {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/midterm";
    private static final String USER = "macerr";
    private static final String PASSWORD = "password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
}
