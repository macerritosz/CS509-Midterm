import com.google.inject.Inject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceBuilder implements IUserServiceBuilder{
    @Inject
    public ServiceBuilder(){}

    @Override
    public IUserService createUserService(Connection conn, String accountType, ResultSet result) throws SQLException {
        if(accountType.equals("ADMIN")) {
            System.out.println("Admin Login Successful");
            return new Administrator(conn);
        } else {
            System.out.println("Customer Login Successful");
            return new CustomerService(createCustomerFromResultSet(result, conn));
        }
    }

    public Customer createCustomerFromResultSet(ResultSet resultSet, Connection conn) throws SQLException {
        int accountID = resultSet.getInt("account_ID");
        int balance = resultSet.getInt("balance");
        return new Customer(accountID, balance, conn);
    }
}
