import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IUserServiceBuilder {
     IUserService createUserService(Connection conn, String accountType, ResultSet result) throws SQLException;
}
