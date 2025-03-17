import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnection implements DatabaseConnection {

    @Override
    public Connection getConnection() throws SQLException {
        return MySQLSource.getConnection();
    }
}
