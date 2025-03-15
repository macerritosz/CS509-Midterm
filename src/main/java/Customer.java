import java.sql.ResultSet;

public class Customer implements IUserType{
    private final ResultSet resultSet;

    public Customer(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
