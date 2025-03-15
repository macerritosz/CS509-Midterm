import java.sql.ResultSet;

public class Administrator implements IUserType {
    private final ResultSet resultSet;

    public Administrator(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
